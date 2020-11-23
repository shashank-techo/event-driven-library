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

    public void publishToTopic(String topicName, String messageText){
        try {
            XMLMessageProducer prod = jcsmpSession.getMessageProducer(new JCSMPStreamingPublishEventHandler() {

                @Override
                public void responseReceived(String messageID) {
                    System.out.println("Producer received response for msg: " + messageID);
                }

                @Override
                public void handleError(String messageID, JCSMPException e, long timestamp) {
                    System.out.printf("Producer received error for msg: %s@%s - %s%n",
                            messageID,timestamp,e);
                }
            });
            final Topic topic = JCSMPFactory.onlyInstance().createTopic(topicName);
            TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
            msg.setText(messageText);
            prod.send(msg,topic);
        } catch (JCSMPException e) {
            e.printStackTrace();
        }
    }
}
