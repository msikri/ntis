package org.apache.ntis.log.time;

import org.apache.ntis.common.LoggerFactory;
import org.apache.ntis.logging.LogLevel;
import org.apache.ntis.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;

@Component
public class TimedClientInterceptor implements ClientInterceptor {

	private final static String START_TIMED_VAL = "time.start.value";

	private Logger logger = LoggerFactory.getInstance().getLogger();
	
	@Value("${spring.application.name}")
	private String applicationName;

	public TimedClientInterceptor(){
		logger.message(LogLevel.INFO, "Starting TimedClientInterceptor");
	}
	
	@Override
	public boolean handleRequest(MessageContext messageContext)
			throws WebServiceClientException {
		long start = System.currentTimeMillis();
		messageContext.setProperty(START_TIMED_VAL, start);
		return true;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext)
			throws WebServiceClientException {
		readResponse(messageContext);
		return true;
	}

	private void readResponse(MessageContext context) {
		long start = (long) context.getProperty(START_TIMED_VAL);
		long end = System.currentTimeMillis();
		long timeTaken = end - start;
		logger.message(LogLevel.METRICS, "[TimedClientInterceptor][" + end
				+ "] TimeTaken=" + timeTaken);
	}

	@Override
	public boolean handleFault(MessageContext messageContext)
			throws WebServiceClientException {
		readResponse(messageContext);
		return true;
	}

}
