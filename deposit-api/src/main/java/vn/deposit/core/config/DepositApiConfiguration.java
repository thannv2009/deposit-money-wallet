package vn.deposit.core.config;

import org.springframework.context.annotation.Configuration;
import vn.core.socket.config.EnableTcpCore;

@Configuration
@EnableDepositCoreConfiguration
@EnableTcpCore
public class DepositApiConfiguration {

}
