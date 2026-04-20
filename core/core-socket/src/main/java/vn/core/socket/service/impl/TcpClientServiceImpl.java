package vn.core.socket.service.impl;

import org.springframework.stereotype.Service;
import vn.core.socket.service.TcpClientService;
import vn.core.socket.service.TcpGateway;

@Service
public class TcpClientServiceImpl implements TcpClientService {

    private final TcpGateway tcpGateway;

    public TcpClientServiceImpl(TcpGateway tcpGateway) {
        this.tcpGateway = tcpGateway;
    }

    @Override
    public String send(String message) {
       return tcpGateway.send(message);
    }

}
