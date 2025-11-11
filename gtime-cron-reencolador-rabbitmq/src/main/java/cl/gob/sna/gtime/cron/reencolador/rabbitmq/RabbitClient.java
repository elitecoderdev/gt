package cl.gob.sna.gtime.cron.reencolador.rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitClient {
	
	private static final String EX_TYPE_DIRECT = "direct";
	private static final String QCOURIER_PSW = System.getenv("RABBITMQ_PASSWORD") != null ? System.getenv("RABBITMQ_PASSWORD") : "CHANGE_ME";
	private static final String QCOURIER_USR = System.getenv("RABBITMQ_USERNAME") != null ? System.getenv("RABBITMQ_USERNAME") : "CHANGE_ME";
	private static final int PORT_5672 = 5672;
	private static final String LOCALHOST = System.getenv("RABBITMQ_HOST") != null ? System.getenv("RABBITMQ_HOST") : "CHANGE_ME";
	private static final String GTIME_INGRESO_QUEUE = "gtimeIngreso";
	private static final String ROUTING_KEY_INGRESO = "ingreso";
	private static final String GTIME_EXCHANGE = "gtimeExchange";
	
	public RabbitClient() {
		//
	}
	
	public void sendToRabbit(String xml) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(LOCALHOST);
		factory.setPort(PORT_5672);
		factory.setUsername(QCOURIER_USR);
		factory.setPassword(QCOURIER_PSW);
		com.rabbitmq.client.Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(GTIME_EXCHANGE, EX_TYPE_DIRECT, true);
		channel.queueDeclare(GTIME_INGRESO_QUEUE, true, false, false, null);
		channel.basicPublish(GTIME_EXCHANGE, ROUTING_KEY_INGRESO, null, xml.getBytes());
		channel.close();
		connection.close();
	}
}