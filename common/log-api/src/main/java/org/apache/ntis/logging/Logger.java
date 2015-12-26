package org.apache.ntis.logging;

public interface Logger {

	void message(LogLevel l, Object message);

	void message(LogLevel l, Object message, Throwable t);
}
