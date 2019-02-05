package cn.e3mall.testMyMessageListener;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestMyMessageListener {
	
	@SuppressWarnings("resource")
	@Test
	public void test() throws Exception {
		new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		System.in.read();
	}
	
}
