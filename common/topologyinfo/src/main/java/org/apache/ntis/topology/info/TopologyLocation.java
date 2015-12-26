package org.apache.ntis.topology.info;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/** Specifies the location of a particular service. */
@Scope(value = "threadscope", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class TopologyLocation {

	private String location;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
