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
import java.util.concurrent.CountDownLatch;

@Component
public class QueuePublisher {

    Logger logger = LoggerFactory.getLogger(QueuePublisher.class);

    @Autowired
    JCSMPSession jcsmpSession;

    public void publishToQueue(String queueName, List<String> messageList){
        final LinkedList<MessageInfo> msgList = new LinkedList<>();
//        CountDownLatch countDownLatch = new CountDownLatch(messageList.size());
        try {
            final Queue queue = JCSMPFactory.onlyInstance().createQueue(queueName);
            final EndpointProperties endpointProps = new EndpointProperties();

            //setting queue properties to consume and exclusive
            endpointProps.setPermission(EndpointProperties.PERMISSION_CONSUME);
            endpointProps.setAccessType(EndpointProperties.ACCESSTYPE_EXCLUSIVE);

            // Actually provision it, and do not fail if it already exists
            jcsmpSession.provision(queue, endpointProps, JCSMPSession.FLAG_IGNORE_ALREADY_EXISTS);

            for (int i = 1; i <= messageList.size(); i++) {
                QueuePublisherCallback queuePublisherCallback = new QueuePublisherCallback();
//                queuePublisherCallback.setCountDownLatch(countDownLatch);
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
//        try {
//            countDownLatch.await();
//        } catch (InterruptedException e) {
//            logger.info("queue thread interrupted..");
//        }
        // Process the replies
        while (msgList.peek() != null) {
            final MessageInfo messageInfo = msgList.poll();
            logger.info("Removing acknowledged message ( {} ) from application list.\n", messageInfo);
        }

    }

}
