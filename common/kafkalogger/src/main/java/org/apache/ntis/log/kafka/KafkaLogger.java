package org.apache.ntis.log.kafka;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.apache.ntis.common.LoggerFactory;
import org.apache.ntis.log.simple.SimpleLogger;
import org.apache.ntis.logging.LogLevel;
import org.apache.ntis.logging.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class KafkaLogger implements Logger, InitializingBean, DisposableBean {

	Producer<String, String> producer;
	private String name = null;
	private String brokerlist = null;

	private void connect() {
		Logger log = LoggerFactory.getInstance().getLogger();
		log.message(LogLevel.INFO, "Starting KAFKA LOGGER.....");
		log.message(LogLevel.INFO, "Kafka connection properties=" + brokerlist);

		Properties props = new Properties();

		// props.put("metadata.broker.list", "broker1:9092,broker2:9092");
		props.put("metadata.broker.list", brokerlist);
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		// props.put("partitioner.class", "example.producer.SimplePartitioner");
		props.put("request.required.acks", "1");
		props.put("producer.type", "async");

		ProducerConfig config = new ProducerConfig(props);
		producer = new Producer<String, String>(config);

	}

	private void close() {
		producer.close();
	}

	@Override
	public void message(LogLevel l, Object message) {
		message(l, message, null);
	}

	@Override
	public void message(LogLevel l, Object message, Throwable t) {

		String msg = "[" + l.toString() + "][" + name + "]"
				+ message.toString();

		if (t != null) {

			msg += t.getMessage();
		}

		KeyedMessage<String, String> data = new KeyedMessage<String, String>(
				"kafka", msg);

		producer.send(data);
	}

	@Override
	public void destroy() throws Exception {
		LoggerFactory.getInstance().removeLogger(this);
		close();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(name, "Name cannot be null");
		Assert.notNull(brokerlist, "Kafka Broker list is null");
		connect();
		LoggerFactory.getInstance().addLogger(this);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBrokerlist(String brokerlist) {
		this.brokerlist = brokerlist;
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof KafkaLogger) {
			return true;
		}
		return false;
	}
}
