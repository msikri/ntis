package org.apache.ntis.topology.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;

@Component
public class TopologyDelayInterceptor implements ClientInterceptor {

	private String source;

	@Autowired
	private TopologyInfoBean infoBean;

	@Autowired
	private TopologyLocation location;

	@Override
	public boolean handleRequest(MessageContext messageContext)
			throws WebServiceClientException {

		String destination = location.getLocation();
		System.out.println("Find Delay for " + source + " to " + destination);
		long delay = infoBean.getDelayForTopology(source, destination);
		System.out.println("Delay for " + source + " to " + destination
				+ " is " + delay);

		if (delay > 0)
			try {
				Thread.currentThread().sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		return true;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext)
			throws WebServiceClientException {
		return true;
	}

	@Override
	public boolean handleFault(MessageContext messageContext)
			throws WebServiceClientException {
		return true;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
