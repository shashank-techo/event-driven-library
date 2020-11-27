package id.co.xl.techolution.eventdrivenlib.config;

import com.solacesystems.jcsmp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to setup connection to solace broker
 *
 * @author  shashank shanu
 *
 * */
@Configuration
public class JCSMPConfig{

    Logger logger = LoggerFactory.getLogger(JCSMPConfig.class);

    @Value("${solace.vpn}")
    private String solaceVpn;
    @Value("${solace.host}")
    private String solaceHost;
    @Value("${solace.username}")
    private String solaceUsername;
    @Value("${solace.password}")
    private String solacePassword;
    @Value("${solace.java.connectRetries}")
    private String solaceConnectRetries;
    @Value("${solace.java.reconnectRetries}")
    private String solaceReconnectRetries;
    @Value("${solace.java.reconnectRetryWaitInMillis}")
    private String solaceReconnectRetrywait;

    /**
     * connection configuration for a session
     *
     * */
    @Bean
    public JCSMPSession getSession() throws InvalidPropertiesException, JCSMPException{
        final JCSMPProperties jcsmpProperties = new JCSMPProperties();
        jcsmpProperties.setProperty(JCSMPProperties.HOST, solaceHost);
        jcsmpProperties.setProperty(JCSMPProperties.USERNAME, solaceUsername);
        jcsmpProperties.setProperty(JCSMPProperties.VPN_NAME,  solaceVpn);
        jcsmpProperties.setProperty(JCSMPProperties.PASSWORD, solacePassword);
        JCSMPChannelProperties jcsmpChannelProperties = new JCSMPChannelProperties();
        jcsmpChannelProperties.setConnectRetries(Integer.parseInt(solaceConnectRetries));
        jcsmpChannelProperties.setReconnectRetries(Integer.parseInt(solaceReconnectRetries));
        jcsmpChannelProperties.setReconnectRetryWaitInMillis(Integer.parseInt(solaceReconnectRetrywait));
//        jcsmpProperties.setProperty(JCSMPProperties.ACK_EVENT_MODE, JCSMPProperties.SUPPORTED_ACK_EVENT_MODE_PER_MSG);
        jcsmpProperties.setProperty(JCSMPProperties.CLIENT_CHANNEL_PROPERTIES,jcsmpChannelProperties);
        JCSMPSession jcsmpSession = JCSMPFactory.onlyInstance().createSession(jcsmpProperties);
        jcsmpSession.connect();
        return jcsmpSession;
    }

}
