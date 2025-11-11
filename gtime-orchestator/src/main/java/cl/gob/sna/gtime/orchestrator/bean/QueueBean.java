package cl.gob.sna.gtime.orchestrator.bean;

import cl.gob.sna.gtime.orchestrator.vo.queue.QueueGtime;
import com.rabbitmq.client.*;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class QueueBean {

	private static final Logger log = LoggerFactory.getLogger(QueueBean.class);
	private final static String QUEUE_NAME = "testQueue";

	/*@Autowired
	private QueueRepository repoQueue;*/
	//int MAXIMO_PAYLOAD_PROCESA_PENDIENTE = 1;

	/*public void revisaPendientes(Exchange exchange) throws NoSuchAlgorithmException, KeyManagementException, IOException, TimeoutException {
		String user = "AdminSNARabbitMQ_GTIMES";
		String pw = "AdminSNARabbitMQ_GTIMES.22";
		String host = "b-059b8675-4b31-4a15-a5dc-a1133b9a26f6.mq.us-east-1.amazonaws.com";

        /*System.setProperty("https.protocols", "SSLv3,TLSv1,TLSv1.1,TLSv1.2");
        SSLContext ctx = SSLContext.getInstance("TLSv1.2");
        ctx.init(null, null, null);
        SSLContext.setDefault(ctx);


		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername(user);
		factory.setPassword(pw);
		factory.setHost(host);
		factory.setPort(5671);
		factory.useSslProtocol();

		Connection connection = factory.newConnection();

		Channel channel = connection.createChannel();

		DefaultConsumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(
					String consumerTag,
					Envelope envelope,
					AMQP.BasicProperties properties,
					byte[] body) throws IOException {

				String message = new String(body, "UTF-8");
				System.out.println("Consumed: " + message);
			}
		};
		channel.basicConsume(QUEUE_NAME, true, consumer);

	}*/
}
