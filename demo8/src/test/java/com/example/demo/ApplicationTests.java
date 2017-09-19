package com.example.demo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ApplicationTests {
 
	@Autowired
	private PropertiesDemo propertiesDemo;
	
	@Test
	public void getHello() throws Exception {
		Assert.assertEquals(propertiesDemo.getName(), "demo");
		Assert.assertEquals(propertiesDemo.getVersion(), "1.0");
		Assert.assertEquals(propertiesDemo.getPort(), 8080);
		Assert.assertEquals(propertiesDemo.getUrl(), "http://localhost:8080");
		
		System.out.println(propertiesDemo);
		
	}
}
