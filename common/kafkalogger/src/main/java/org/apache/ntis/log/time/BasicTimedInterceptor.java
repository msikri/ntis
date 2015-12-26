package org.apache.ntis.log.time;

import org.apache.ntis.common.LoggerFactory;
import org.apache.ntis.logging.LogLevel;
import org.apache.ntis.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BasicTimedInterceptor {

	private ThreadLocal<Long> startTime = new ThreadLocal<Long>();

	@Value("${spring.application.name}")
	private String applicationName;

	private Logger logger = LoggerFactory.getInstance().getLogger();

	public BasicTimedInterceptor(){
		logger.message(LogLevel.INFO, "Starting BasicTimedInterceptor");
	}
	
	public void startMethod() {
		startTime.set(System.currentTimeMillis());
	}

	public void endMethod(String source) {
		long end = System.currentTimeMillis();
		long timeTaken = end - startTime.get();
		logger.message(LogLevel.METRICS, "[" + end + "][" + source
				+ "] TimeTaken=" + timeTaken);
	}
}
