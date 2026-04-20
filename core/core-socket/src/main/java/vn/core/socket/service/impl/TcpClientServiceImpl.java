package vn.core.socket.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.core.socket.service.TcpClientService;
import vn.core.socket.service.TcpGateway;

@Service
public class TcpClientServiceImpl implements TcpClientService {

    @Autowired
    private TcpGateway tcpGateway;

    @Override
    public String send(String message) {
        return tcpGateway.send(message);
    }

}
