package org.apache.ntis.log.simple;

import org.apache.ntis.logging.LogLevel;
import org.apache.ntis.logging.Logger;

public class SimpleLogger implements Logger {

	public synchronized void message(LogLevel l, Object message) {
		System.out.println("[SIMPLE]"+message);
	}

	public synchronized void message(LogLevel l, Object message,
			Throwable t) {
		System.out.print("[SIMPLE]"+message);
		t.printStackTrace(System.out);
	}

}
