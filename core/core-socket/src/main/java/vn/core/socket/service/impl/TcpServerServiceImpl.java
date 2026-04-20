package vn.core.socket.service.impl;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;
import vn.core.socket.service.TcpServiceService;

@Service
public class TcpServerServiceImpl implements TcpServiceService {

    @ServiceActivator(inputChannel = "requestChannel", outputChannel = "replyChannel")
    public String handle(String message) {

        System.out.println("Received: " + message);

        return "REPLY: " + message;
    }

}
