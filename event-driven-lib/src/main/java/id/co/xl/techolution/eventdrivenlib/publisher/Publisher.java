package id.co.xl.techolution.eventdrivenlib.publisher;

import com.solacesystems.jcsmp.*;
import id.co.xl.techolution.eventdrivenlib.model.MessageInfo;
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
     * @param           jcsmpStreamingPublishCorrelatingEventHandler
     *
     *
     * */
    public void publishToTopic(String topicName,DeliveryMode deliveryMode, String messageText, int retryCount, JCSMPStreamingPublishCorrelatingEventHandler jcsmpStreamingPublishCorrelatingEventHandler){
        try {
            XMLMessageProducer prod = jcsmpSession.getMessageProducer(jcsmpStreamingPublishCorrelatingEventHandler);
            final Topic topic = JCSMPFactory.onlyInstance().createTopic(topicName);
            TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
            msg.setText(messageText);
            msg.setDeliveryMode(deliveryMode);
            msg.setAckImmediately(deliveryMode.equals(DeliveryMode.DIRECT)?Boolean.FALSE:Boolean.TRUE);
            final MessageInfo msgCorrelationInfo = new MessageInfo(1);
            msgCorrelationInfo.sessionIndependentMessage = msg;
            msgCorrelationInfo.topicName = topicName;
            msgCorrelationInfo.message = msg.getText();
            msgCorrelationInfo.attemptsLeft = retryCount;
            msgCorrelationInfo.deliveryMode = deliveryMode;
            msg.setCorrelationKey(msgCorrelationInfo);
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
     * @param           jcsmpStreamingPublishCorrelatingEventHandler
     *
     * */
    public void publishToTopic(String topicName, TextMessage textMessage, int retryCount, JCSMPStreamingPublishCorrelatingEventHandler jcsmpStreamingPublishCorrelatingEventHandler){
        try {
            XMLMessageProducer prod = jcsmpSession.getMessageProducer(jcsmpStreamingPublishCorrelatingEventHandler);
            final Topic topic = JCSMPFactory.onlyInstance().createTopic(topicName);
            final MessageInfo msgCorrelationInfo = new MessageInfo(1);
            msgCorrelationInfo.sessionIndependentMessage = textMessage;
            msgCorrelationInfo.topicName = topicName;
            msgCorrelationInfo.message = textMessage.getText();
            msgCorrelationInfo.attemptsLeft = retryCount;
            textMessage.setCorrelationKey(msgCorrelationInfo);
            prod.send(textMessage,topic);
        } catch (JCSMPException e) {
            e.printStackTrace();
        }
    }


}
