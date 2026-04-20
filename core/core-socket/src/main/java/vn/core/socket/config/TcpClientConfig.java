package vn.core.socket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.ip.tcp.TcpOutboundGateway;
import org.springframework.integration.ip.tcp.connection.TcpNetClientConnectionFactory;
import org.springframework.integration.ip.tcp.serializer.ByteArrayLengthHeaderSerializer;
import org.springframework.messaging.MessageChannel;

//Gửi request qua TCP IP

@Configuration
public class TcpClientConfig {

    // =====================
    // 1. Connection Factory
    // =====================
    @Bean
    public TcpNetClientConnectionFactory clientFactory() {
        TcpNetClientConnectionFactory factory =
                new TcpNetClientConnectionFactory("localhost", 9999);

        factory.setConnectTimeout(3000);
        factory.setSoTimeout(5000);

        // Serializer / Deserializer
        factory.setSerializer(codec());
        factory.setDeserializer(codec());

        return factory;
    }

    // =====================
    // 2. Codec (encode/decode)
    // =====================
    @Bean
    public ByteArrayLengthHeaderSerializer codec() {
        return new ByteArrayLengthHeaderSerializer();
    }

    // =====================
    // 3. Outbound Gateway (send & receive)
    // =====================
    @Bean
    @ServiceActivator(inputChannel = "tcpOutChannel")
    public TcpOutboundGateway tcpOutboundGateway() {
        TcpOutboundGateway gateway = new TcpOutboundGateway();
        gateway.setConnectionFactory(clientFactory());
        gateway.setRequestTimeout(5000);
        return gateway;
    }

    // =====================
    // 4. Channel
    // =====================
    @Bean
    public MessageChannel tcpOutChannel() {
        return new DirectChannel();
    }


}
