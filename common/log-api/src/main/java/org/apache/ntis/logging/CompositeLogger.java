package org.apache.ntis.logging;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CompositeLogger implements Logger {

	private List<Logger> loggers = new CopyOnWriteArrayList<Logger>();

	@Override
	public void message(LogLevel l, Object message) {
		for (Logger log : loggers) {
			log.message(l, message);
		}
	}

	@Override
	public void message(LogLevel l, Object message, Throwable t) {
		for (Logger log : loggers) {
			log.message(l, message, t);
		}
	}

	public void addLogger(Logger l) {
		if (!loggers.contains(l))
			loggers.add(l);
	}

	public void removeLogger(Logger l) {
		loggers.remove(l);
	}
}
