package com.example.microservice;

import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPSession;
import id.co.xl.techolution.eventdrivenlib.config.JCSMPConfig;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class MicroserviceApplicationTests {

//	@MockBean
//	JCSMPSession jcsmpSession;
//
//	@Mock
//	JCSMPConfig jcsmpConfig;

//	@MockBean
//	Publisher publisher;
//
//	@MockBean
//	Subscriber subscriber;
//
//	@InjectMocks
//	QueuePublisher queuePublisher;
//
//	@Mock
//	QueueSubscriber queueSubscriber;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private JCSMPConfig jcsmpConfig;

	@Test
	void sampleTestCases() throws JCSMPException {
        String solaceVpn = applicationContext.getEnvironment().getProperty("solace.vpn");
        String solaceHost = applicationContext.getEnvironment().getProperty("solace.host");
        String solaceUsername = applicationContext.getEnvironment().getProperty("solace.username");
        String solacePassword = applicationContext.getEnvironment().getProperty("solace.password");
        JCSMPSession jcsmpSession = jcsmpConfig.getSession();

        Assert.assertEquals(jcsmpSession.getProperty("host").toString(),solaceHost);
        Assert.assertEquals(jcsmpSession.getProperty("username").toString(),solaceUsername);
        Assert.assertEquals(jcsmpSession.getProperty("vpn_name").toString(),solaceVpn);
        Assert.assertEquals(jcsmpSession.getProperty("password").toString(),solacePassword);
	}

}
