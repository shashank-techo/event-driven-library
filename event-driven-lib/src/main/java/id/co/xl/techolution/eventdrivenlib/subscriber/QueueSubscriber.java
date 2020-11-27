package id.co.xl.techolution.eventdrivenlib.subscriber;

import com.solacesystems.jcsmp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueueSubscriber {

    Logger logger = LoggerFactory.getLogger(QueueSubscriber.class);

    @Autowired
    JCSMPSession jcsmpSession;

    /**
     * method to subscribe to a queue
     * @param           queueName
     * @param           xmlMessageListener
     *
     * */
    public void queueListener(String queueName, XMLMessageListener xmlMessageListener) throws JCSMPException {
        final Queue queue = JCSMPFactory.onlyInstance().createQueue(queueName);
        // Create a Flow be able to bind to and consume messages from the Queue.
        final ConsumerFlowProperties consumerFlowProperties = new ConsumerFlowProperties();
        consumerFlowProperties.setEndpoint(queue);
        consumerFlowProperties.setAckMode(JCSMPProperties.SUPPORTED_MESSAGE_ACK_CLIENT);
        EndpointProperties endpointProperties = new EndpointProperties();
        endpointProperties.setAccessType(EndpointProperties.ACCESSTYPE_EXCLUSIVE);
        final FlowReceiver cons = jcsmpSession.createFlow(xmlMessageListener, consumerFlowProperties, endpointProperties);
        logger.info("Connected. Awaiting message ...");
        cons.start();
    }

}
