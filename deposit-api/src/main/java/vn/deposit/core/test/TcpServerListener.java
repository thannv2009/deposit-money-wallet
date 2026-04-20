package vn.deposit.core.test;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
public class TcpServerListener {

    @ServiceActivator(inputChannel = "requestChannel", outputChannel = "replyChannel")
    public void handle(String message) {
        System.out.println("sdasdsa");
    }
}
