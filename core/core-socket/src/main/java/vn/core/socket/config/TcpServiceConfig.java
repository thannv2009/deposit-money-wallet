package vn.core.socket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.integration.ip.tcp.serializer.ByteArrayLengthHeaderSerializer;
import org.springframework.messaging.MessageChannel;

// Nhận request thông qua TCP IP
@Configuration
public class TcpServiceConfig {

    // =====================
    // 1. Server Factory
    // =====================
    @Bean
    public TcpNetServerConnectionFactory serverFactory() {
        TcpNetServerConnectionFactory factory =
                new TcpNetServerConnectionFactory(9999);

        factory.setSerializer(codec());
        factory.setDeserializer(codec());

        return factory;
    }

    // =====================
    // 2. Codec
    // =====================
    @Bean
    public ByteArrayLengthHeaderSerializer codec() {
        return new ByteArrayLengthHeaderSerializer();
    }

    // =====================
    // 3. Inbound Adapter
    // =====================
    @Bean
    public TcpInboundGateway tcpInboundGateway() {
        TcpInboundGateway gateway = new TcpInboundGateway();
        gateway.setConnectionFactory(serverFactory());
        gateway.setRequestChannel(requestChannel());
        gateway.setReplyChannel(replyChannel());
        return gateway;
    }

    @Bean
    public MessageChannel requestChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel replyChannel() {
        return new DirectChannel();
    }


}
