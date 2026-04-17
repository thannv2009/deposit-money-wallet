package vn.deposit.schedule.cron;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.deposit.schedule.component.ReconciliationComponent;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Component
@Slf4j
public class ReconciliationCron {

    @Autowired
    private ReconciliationComponent reconciliationComponent; // Service chứa hàm runDailyRecon của bạn

    // Cấu hình đường dẫn thư mục chứa file SFTP (lấy từ application.properties)
    @Value("${sftp.local-dir:/app/sftp/}")
    private String sftpLocalDir;

    @Scheduled(cron = "0 0 2 * * ?") // Chạy vào 2h sáng hàng ngày
    public void scanAndRunJob() {
        log.info("Bắt đầu quét file đối soát...");

        // 1. Tạo tên file mong muốn (Ví dụ: bank_20231027.csv)
        String todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String fileName = "bank_" + todayStr + ".csv";
        File file = new File(sftpLocalDir + fileName);

        // 2. Kiểm tra file có tồn tại không
        if (file.exists() && !file.isDirectory()) {
            log.info("Tìm thấy file: {}. Bắt đầu chạy Job...", file.getAbsolutePath());

            // 3. Gọi hàm xử lý của bạn
            reconciliationComponent.runDailyRecon(file.getAbsolutePath());

        } else {
            log.error("KHÔNG tìm thấy file đối soát ngày hôm nay: {}", file.getAbsolutePath());
            // Có thể gửi tin nhắn cảnh báo Telegram/Mail ở đây
        }
    }

}
