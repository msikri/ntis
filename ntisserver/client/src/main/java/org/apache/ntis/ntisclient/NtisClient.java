package org.apache.ntis.ntisclient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ntis.server.datamodel.GetProximityList;
import org.apache.ntis.server.datamodel.GetProximityListResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class NtisClient implements InitializingBean {
	private RestTemplate restTemplate;

	public GetProximityListResponse callService(GetProximityList request,
			String address) throws RestClientException, URISyntaxException {

		// return (GetProximityListResponse) webServiceTemplate
		// .marshalSendAndReceive(address, list);

		return restTemplate.postForObject(new URI(address), request,
				GetProximityListResponse.class);
//		return restTemplate.getForObject(address, request,
//				GetProximityListResponse.class);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(GetProximityList.class,
				GetProximityListResponse.class);
		MarshallingHttpMessageConverter converter = new MarshallingHttpMessageConverter();
		List<MediaType> medias = new ArrayList<MediaType>();
		medias.add(MediaType.APPLICATION_OCTET_STREAM);
		converter.setSupportedMediaTypes(medias);
		converter.setMarshaller(marshaller);
		converter.setUnmarshaller(marshaller);
		restTemplate.getMessageConverters().add(converter);
		// webServiceTemplate.setMarshaller(marshaller);
		// webServiceTemplate.setUnmarshaller(marshaller);
	}

	public void setRestTemplate(RestTemplate template) {
		this.restTemplate = template;
	}
}
