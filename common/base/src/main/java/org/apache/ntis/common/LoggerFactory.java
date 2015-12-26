package org.apache.ntis.common;

import org.apache.ntis.log.simple.SimpleLogger;
import org.apache.ntis.logging.CompositeLogger;
import org.apache.ntis.logging.Logger;

public class LoggerFactory {

	private static LoggerFactory loggerfactory = new LoggerFactory();
	private CompositeLogger delegateLogger = new CompositeLogger();

	private LoggerFactory() {
		delegateLogger.addLogger(new SimpleLogger());
	}

	public synchronized void addLogger(Logger log) {
		delegateLogger.addLogger(log);
	}
	
	public synchronized void removeLogger(Logger log){
		delegateLogger.removeLogger(log);
	}

	public static LoggerFactory getInstance() {
		return loggerfactory;
	}

	public synchronized Logger getLogger() {

		return delegateLogger;
	}
}
