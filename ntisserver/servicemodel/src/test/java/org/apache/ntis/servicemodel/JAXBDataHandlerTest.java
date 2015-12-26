package org.apache.ntis.servicemodel;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.junit.Test;

public class JAXBDataHandlerTest {

	@Test
	public void testHandleRequestObject() throws IOException, JAXBException {
		InputStream fileStream = JAXBDataHandlerTest.class
				.getResourceAsStream("clientrequest.xml");
		Assert.assertNotNull(fileStream);

		JAXBDataHandler handler = new JAXBDataHandler();

		System.out.println(handler.handleRequestObject(fileStream));

	}

//	@Test
//	public void testHandleResponseObject() {
//		fail("Not yet implemented");
//	}

}
