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

    /**
     * method for publishing data to topic where you can pass delivery mode along with the text message,
     * based on the delivery mode, this method will set the acknowledgement criteriA
     * @param           topicName
     * @param           deliveryMode
     * @param           messageText
     * @param           jcsmpStreamingPublishEventHandler
     *
     *
     * */
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


    /**
     * generic method for publishing data to topic in solace broker where you can setup different different
     * properties for the text message you have passed in the parameter textMessage. You need to wrap
     * your text data/message inside the TextMessage wrapper
     * @param           topicName
     * @param           textMessage
     * @param           jcsmpStreamingPublishEventHandler
     *
     * */
    public void publishToTopic(String topicName, TextMessage textMessage, JCSMPStreamingPublishEventHandler jcsmpStreamingPublishEventHandler){
        try {
            XMLMessageProducer prod = jcsmpSession.getMessageProducer(jcsmpStreamingPublishEventHandler);
            final Topic topic = JCSMPFactory.onlyInstance().createTopic(topicName);
            prod.send(textMessage,topic);
        } catch (JCSMPException e) {
            e.printStackTrace();
        }
    }


}
