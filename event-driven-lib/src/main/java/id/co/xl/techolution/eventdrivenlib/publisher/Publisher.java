package id.co.xl.techolution.eventdrivenlib.publisher;

import com.solacesystems.jcsmp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Publisher component of solace topic
 *
 * @author  shashank shanu
 *
 * */
@Component
public class Publisher {

    Logger logger = LoggerFactory.getLogger(Publisher.class);

    @Autowired
    JCSMPSession jcsmpSession;

    public void publishToTopic(String topicName,DeliveryMode deliveryMode, String messageText, JCSMPStreamingPublishEventHandler jcsmpStreamingPublishEventHandler){
        try {
            XMLMessageProducer prod = jcsmpSession.getMessageProducer(jcsmpStreamingPublishEventHandler);
            final Topic topic = JCSMPFactory.onlyInstance().createTopic(topicName);
            TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
            msg.setText(messageText);
            msg.setDeliveryMode(deliveryMode);
            msg.setAckImmediately(deliveryMode.equals(DeliveryMode.DIRECT)?Boolean.FALSE:Boolean.TRUE);
            prod.send(msg,topic);
        } catch (JCSMPException e) {
            e.printStackTrace();
        }
    }


}
