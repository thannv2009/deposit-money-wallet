package vn.deposit.schedule.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReconciliationComponent {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job reconJob;

    public void runDailyRecon(String filePath) {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("filePath", filePath)
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(reconJob, params);
            log.info("Đối soát hoàn tất cho file: " + filePath);
        } catch (Exception e) {
            log.error("Lỗi khi chạy Job đối soát: ", e);
        }
    }


}
