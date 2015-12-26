package org.eclipse.ntis.server;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.apache.ntis.server.JAXBWSServlet;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

public class JAXBWSServletTest {
	private Server server;

	private int PORT = 9090;

	@Before
	public void begin() throws Exception {
		server = new Server(PORT);
		Context root = new Context(server, "/ntis", Context.SESSIONS);
		root.addServlet(new ServletHolder(new JAXBWSServlet()), "/*");
		server.start();
	}

	@After
	public void shutdown() throws Exception {
		server.stop();
	}

	@Test
	public void doGet() throws Exception {
		URL u = new URL("http://localhost:" + PORT + "/ntis/");

		InputStream inStream = u.openConnection().getInputStream();
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byte[] tmp = new byte[100];
		int len = inStream.read(tmp);
		while (len > 0) {
			byteStream.write(tmp, 0, len);
			len = inStream.read(tmp);
		}
		inStream.close();
		byteStream.close();

		String s = new String(byteStream.toByteArray());
		System.out.println("MESSAGE=" + s);
		Assert.assertTrue(s.startsWith(JAXBWSServlet.MESSAGE));
	}

	@Test
	public void doWebPut() throws Exception {
		URL u = new URL("http://localhost:" + PORT + "/ntis/");
		URLConnection connection = u.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		OutputStream outStream = connection.getOutputStream();
		InputStream fileStream = JAXBWSServletTest.class
				.getResourceAsStream("clientrequest.xml");

		IOUtils.copy(fileStream, outStream);

		InputStream inStream = connection.getInputStream();

		IOUtils.closeQuietly(fileStream);
		IOUtils.closeQuietly(outStream);
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

		IOUtils.copy(inStream, byteStream);

		IOUtils.closeQuietly(inStream);
		IOUtils.closeQuietly(byteStream);

		String s = new String(byteStream.toByteArray());
		System.out.println("MESSAGE=" + s);

		fileStream = JAXBWSServletTest.class
				.getResourceAsStream("clientresponse.xml");
		String exp = IOUtils.toString(fileStream);

		XMLUnit.compareXML(exp, s);

	}

	public static void main(String args[]) {
		JAXBWSServletTest t = new JAXBWSServletTest();
		try {
//			t.begin();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
