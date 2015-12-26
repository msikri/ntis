package org.apache.ntis.application;

import org.apache.ntis.application.test.TestApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { TestApplication.class })
public class ApplicationTest {

	@Autowired
	private Application app;

	@Test
	public void hello() {
		Assert.assertEquals("BaseApplication", app.getApplication());
	}
}
