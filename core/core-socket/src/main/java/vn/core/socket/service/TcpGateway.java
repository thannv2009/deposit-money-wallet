package vn.core.socket.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface TcpGateway {

    @Gateway(requestChannel = "tcpOutChannel")
    String send(String data);

}
