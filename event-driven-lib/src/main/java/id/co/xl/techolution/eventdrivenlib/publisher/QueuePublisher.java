package id.co.xl.techolution.eventdrivenlib.publisher;

import com.solacesystems.jcsmp.*;
import id.co.xl.techolution.eventdrivenlib.model.MessageInfo;
import id.co.xl.techolution.eventdrivenlib.model.QueuePublisherCallback;
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
    public void publishToQueue(String queueName, List<String> messageList){
        final LinkedList<MessageInfo> msgList = new LinkedList<>();
        try {
            final Queue queue = JCSMPFactory.onlyInstance().createQueue(queueName);

            for (int i = 1; i <= messageList.size(); i++) {
                QueuePublisherCallback queuePublisherCallback = new QueuePublisherCallback();
                XMLMessageProducer prod = jcsmpSession.getMessageProducer(queuePublisherCallback);
                TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
                msg.setDeliveryMode(DeliveryMode.PERSISTENT);
                msg.setText(messageList.get(i-1));
                final MessageInfo msgCorrelationInfo = new MessageInfo(i);
                msgCorrelationInfo.sessionIndependentMessage = msg;
                msgList.add(msgCorrelationInfo);
                msg.setCorrelationKey(msgCorrelationInfo);
                prod.send(msg,queue);
            }
        } catch (JCSMPException e) {
            e.printStackTrace();
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
    public void publishToQueue(String queueName, String message){
        try {
            final Queue queue = JCSMPFactory.onlyInstance().createQueue(queueName);
            QueuePublisherCallback queuePublisherCallback = new QueuePublisherCallback();
            XMLMessageProducer prod = jcsmpSession.getMessageProducer(queuePublisherCallback);
            TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
            msg.setDeliveryMode(DeliveryMode.PERSISTENT);
            msg.setText(message);
            prod.send(msg,queue);

        } catch (JCSMPException e) {
            e.printStackTrace();
        }

    }
}
