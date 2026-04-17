package vn.deposit.schedule.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.deposit.schedule.dto.BankTransactionDTO;

import javax.sql.DataSource;

@Configuration
public class DepositScheduleConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    // --- 1. READER: Đọc file động dựa trên path truyền vào ---
    @Bean
    @StepScope
    public FlatFileItemReader<BankTransactionDTO> reader(@Value("#{jobParameters['filePath']}") String pathToFile) {
        return new FlatFileItemReaderBuilder<BankTransactionDTO>()
                .name("bankReader")
                .resource(new FileSystemResource(pathToFile))
                .delimited()
                .names("bankTrxId", "amount", "status", "traceId")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(BankTransactionDTO.class);
                }})
                .build();
    }

    // --- 2. WRITER: Ghi vào bảng Staging (tốc độ cao) ---
    @Bean
    public JdbcBatchItemWriter<BankTransactionDTO> writer() {
        return new JdbcBatchItemWriterBuilder<BankTransactionDTO>()
                .dataSource(dataSource)
                .sql("INSERT INTO staging_bank_records (bank_trx_id, amount, status, trace_id) " +
                        "VALUES (:bankTrxId, :amount, :status, :traceId)")
                .beanMapped()
                .build();
    }

    // --- 3. TASKLET: Logic so sánh bằng SQL (Triệu bản ghi xử lý trong vài giây) ---
    @Bean
    public MethodInvokingTaskletAdapter compareTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();
        adapter.setTargetObject(new Object() {
            public void execute() {
                JdbcTemplate jdbc = new JdbcTemplate(dataSource);
                // Đánh dấu khớp
                jdbc.execute("UPDATE gateway_transactions g SET recon_status = 'MATCHED' " +
                        "WHERE EXISTS (SELECT 1 FROM staging_bank_records b WHERE g.trace_id = b.trace_id AND g.amount = b.amount)");
                // Tìm bản ghi lệch
                jdbc.execute("INSERT INTO recon_diff (trace_id, reason) " +
                        "SELECT g.trace_id, 'MISMATCH' FROM gateway_transactions g " +
                        "JOIN staging_bank_records b ON g.trace_id = b.trace_id WHERE g.amount != b.amount");
                // Tìm bản ghi không tồn tại
                jdbc.execute("INSERT INTO recon_diff_reports (trace_id, reason)\n" +
                        "SELECT b.trace_id, 'Bank có nhưng Gateway không có'\n" +
                        "FROM staging_bank_records b\n" +
                        "LEFT JOIN gateway_transactions g ON b.trace_id = g.trace_id\n" +
                        "WHERE g.trace_id IS NULL");
            }
        });
        adapter.setTargetMethod("execute");
        return adapter;
    }

    // --- 4. JOB DEFINITION ---
    @Bean
    public Job reconJob(Step loadStep, Step compareStep) {
        return jobBuilderFactory.get("reconJob_" + System.currentTimeMillis())
                .start(loadStep)
                .next(compareStep)
                .build();
    }

    @Bean
    public Step loadStep() {
        return stepBuilderFactory.get("loadStep")
                .<BankTransactionDTO, BankTransactionDTO>chunk(5000) // Mỗi 5000 dòng commit 1 lần
                .reader(reader(null))
                .writer(writer())
                .faultTolerant()
                .skip(FlatFileParseException.class)
                .skipLimit(100)
                .build();
    }

    @Bean
    public Step compareStep() {
        return stepBuilderFactory.get("compareStep")
                .tasklet(compareTasklet())
                .build();
    }

}
