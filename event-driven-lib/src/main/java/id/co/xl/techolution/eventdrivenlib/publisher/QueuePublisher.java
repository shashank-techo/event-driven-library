package id.co.xl.techolution.eventdrivenlib.publisher;

import com.solacesystems.jcsmp.*;
import id.co.xl.techolution.eventdrivenlib.model.MessageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;


@Component
public class QueuePublisher {

    Logger logger = LoggerFactory.getLogger(QueuePublisher.class);

    @Autowired
    JCSMPSession jcsmpSession;
    /**
     * method to publish a list to a queue
     * @param           queueName
     * @param           messageList
     *
     *
     * */
    public void publishToQueue(String queueName, List<String> messageList, int retryCount, JCSMPStreamingPublishCorrelatingEventHandler jcsmpStreamingPublishCorrelatingEventHandler) {
        final LinkedList<MessageInfo> msgList = new LinkedList<>();
        try {
            final Queue queue = JCSMPFactory.onlyInstance().createQueue(queueName);

            for (int i = 1; i <= messageList.size(); i++) {
                XMLMessageProducer prod = jcsmpSession.getMessageProducer(jcsmpStreamingPublishCorrelatingEventHandler);
                TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
                msg.setDeliveryMode(DeliveryMode.PERSISTENT);
                msg.setText(messageList.get(i-1));
                final MessageInfo msgCorrelationInfo = new MessageInfo(i);
                msgCorrelationInfo.sessionIndependentMessage = msg;
                msgCorrelationInfo.methodName = "publishToQueue";
                msgCorrelationInfo.queueName = queueName;
                msgCorrelationInfo.message = msg.getText();
                msgCorrelationInfo.attemptsLeft = retryCount;
                msgList.add(msgCorrelationInfo);
                msg.setCorrelationKey(msgCorrelationInfo);
                prod.send(msg,queue);
            }
        } catch (JCSMPException e) {
           logger.info("queue publisher exception, {}",e);
        }
        // Process the replies
        while (msgList.peek() != null) {
            final MessageInfo messageInfo = msgList.poll();
            logger.info("Removing acknowledged message ( {} ) from application list.\n", messageInfo);
        }
    }

    /**
     * method to publish a message to a queue
     * @param           queueName
     * @param           message
     *
     *
     * */
    public void publishToQueue(String queueName, String message, int retryCount, JCSMPStreamingPublishCorrelatingEventHandler jcsmpStreamingPublishCorrelatingEventHandler){
        try {
            final Queue queue = JCSMPFactory.onlyInstance().createQueue(queueName);
            XMLMessageProducer prod = jcsmpSession.getMessageProducer(jcsmpStreamingPublishCorrelatingEventHandler);
            TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
            msg.setDeliveryMode(DeliveryMode.PERSISTENT);
            msg.setText(message);
            final MessageInfo msgCorrelationInfo = new MessageInfo(1);
            msgCorrelationInfo.sessionIndependentMessage = msg;
            msgCorrelationInfo.methodName = "publishToQueue";   //change the name according to the method you are calling
            msgCorrelationInfo.queueName = queueName;
            msgCorrelationInfo.message = msg.getText();
            msgCorrelationInfo.attemptsLeft = retryCount;
            msg.setCorrelationKey(msgCorrelationInfo);
            prod.send(msg,queue);

        } catch (JCSMPException e) {
            e.printStackTrace();
        }
    }
}
