package id.co.xl.techolution.eventdrivenlib.subscriber;

import com.solacesystems.jcsmp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Subscriber component of solace topic
 *
 * @author  shashank shanu
 *
 * */
@Component
public class Subscriber {

    Logger logger = LoggerFactory.getLogger(Subscriber.class);

    @Autowired
    JCSMPSession jcsmpSession;


    /**
     * method for subscribing to a topic
     * @param               topicName
     * @param               xmlMessageListener
     *
     *
     * */
    public void subscribeTopic(String topicName, XMLMessageListener xmlMessageListener){
        try {
            final XMLMessageConsumer cons = jcsmpSession.getMessageConsumer(xmlMessageListener);
            final Topic topic = JCSMPFactory.onlyInstance().createTopic(topicName);
            jcsmpSession.addSubscription(topic);
            cons.start();
        } catch (JCSMPException e) {
            e.printStackTrace();
        }
    }

}
