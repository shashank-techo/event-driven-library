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

    public void queueListener(String queueName, XMLMessageListener xmlMessageListener) throws JCSMPException {
        logger.info("Attempting to provision the queue {} on the appliance.", queueName);
        final EndpointProperties endpointProps = new EndpointProperties();
        // set queue permissions to "consume" and access-type to "exclusive"
        endpointProps.setPermission(EndpointProperties.PERMISSION_CONSUME);
        endpointProps.setAccessType(EndpointProperties.ACCESSTYPE_EXCLUSIVE);
        // create the queue object locally
        final Queue queue = JCSMPFactory.onlyInstance().createQueue(queueName);
        // Actually provision it, and do not fail if it already exists
        try {
            jcsmpSession.provision(queue, endpointProps, JCSMPSession.FLAG_IGNORE_ALREADY_EXISTS);
        } catch (JCSMPException e) {
            e.printStackTrace();
        }
        logger.info("Attempting to bind to the queue {} on the appliance.", queueName);

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
