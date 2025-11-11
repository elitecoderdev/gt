package cl.gob.sna.gtime.orchestrator.config;

import com.rabbitmq.client.ConnectionFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class RabbitClientConfig {
    Logger log = Logger.getLogger(RabbitClientConfig.class);

    @Value("${gtime.rabbit.http.url}")
    private String URL_HOST;
    @Value("${gtime.rabbit.http.user}")
    private String USR;
    @Value("${gtime.rabbit.http.pass}")
    private String PWD;
    @Value("${gtime.rabbit.http.port}")
    private int PORT;
    @Value("${gtime.rabbit.opt.autorecovery}")
    private boolean AUT_RECOVERY;

    @Bean
    public ConnectionFactory rabbitConnectionFactory() throws NoSuchAlgorithmException, KeyManagementException {
        ConnectionFactory factory = new ConnectionFactory();

        if (PORT == 5671){
            System.setProperty("https.protocols", "SSLv3,TLSv1,TLSv1.1,TLSv1.2");
            SSLContext ctx = SSLContext.getInstance("TLSv1.2");
            ctx.init(null, null, null);
            SSLContext.setDefault(ctx);
            factory.useSslProtocol();
        }

        factory.setUsername(USR);
        factory.setPassword(PWD);
        factory.setHost(URL_HOST);
        factory.setPort(PORT);

        return factory;
    }
}
