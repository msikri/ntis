package org.apache.ntis.servicemodel;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.ntis.server.datamodel.GetProximityList;
import org.apache.ntis.server.datamodel.GetProximityListResponse;

public class JAXBDataHandler {

	public GetProximityList handleRequestObject(InputStream stream)
			throws JAXBException {
		JAXBContext context = JAXBContext
				.newInstance(GetProximityList.class);

		return (GetProximityList) context.createUnmarshaller()
				.unmarshal(stream);
	}

	public void handleResponseObject(GetProximityListResponse response,
			OutputStream outstream) throws JAXBException {
		JAXBContext context = JAXBContext
				.newInstance(GetProximityListResponse.class);

		context.createMarshaller().marshal(response, outstream);
	}
}
