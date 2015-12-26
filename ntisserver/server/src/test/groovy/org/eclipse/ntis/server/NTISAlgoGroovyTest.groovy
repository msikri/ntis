package org.eclipse.ntis.server;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;

import org.apache.ntis.server.datamodel.GetProximityList;
import org.apache.ntis.server.datamodel.GetProximityListResponse;
import org.apache.ntis.server.datamodel.ServiceLocationType;
import org.junit.Test;

class NTISAlgoGroovyTest {
	@Test
	public void simpleCase() {
		GetProximityList request = new GetProximityList();
		request.setClientlocation("DataCenter1");
		
		ServiceLocationType location = new ServiceLocationType();
		
		List<String> site = new ArrayList<String>();
		site.add("DataCenter1")
		site.add("DataCenter2")
		site.add("HeadQuarters")
		location.getSite().addAll(site);
		
		request.setServicelocation(location);
		
		
		NTISAlgo algo = new NTISAlgo();
		GetProximityListResponse response = algo.processMessage(request);
		
		JAXBContext context = JAXBContext.newInstance("org.apache.ntis.server.datamodel");
		Marshaller m =  context.createMarshaller();
		m.setProperty("jaxb.formatted.output", new Boolean(true));
		StreamResult result = new StreamResult( System.out );
		
		println "RESPONSE:"
		//		m.set
		m.marshal( response, result );
		
		
	}
}
