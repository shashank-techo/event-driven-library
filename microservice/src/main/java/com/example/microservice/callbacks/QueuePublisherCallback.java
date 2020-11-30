package com.example.microservice.callbacks;


import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPStreamingPublishCorrelatingEventHandler;
import id.co.xl.techolution.eventdrivenlib.model.MessageInfo;
import id.co.xl.techolution.eventdrivenlib.publisher.QueuePublisher;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component
@Scope( scopeName = "prototype")
public class QueuePublisherCallback implements JCSMPStreamingPublishCorrelatingEventHandler {

    Logger logger = LoggerFactory.getLogger(QueuePublisherCallback.class);

    @Autowired
    QueuePublisher queuePublisher;

    @Autowired
    ApplicationContext applicationContext;

    @Override
    public void responseReceivedEx(Object key) {
        if (key instanceof MessageInfo) {
            MessageInfo i = (MessageInfo) key;
            i.acked = true;
            i.publishedSuccessfully = true;
            logger.info("Queue acknowledgement received for {} ", i);
        }
    }

    @Override
    public void handleErrorEx(Object key, JCSMPException cause, long l) {
        if (key instanceof MessageInfo) {
            MessageInfo i = (MessageInfo) key;
            logger.info("Queue error received for {}, error was {} ", i, cause);
            if(i.attemptsLeft>0){
                logger.info("retrying attempt left {} message {} ", i.attemptsLeft, i.message);
                QueuePublisherCallback queuePublisherCallback = applicationContext.getBean(QueuePublisherCallback.class);
                queuePublisher.publishToQueue(i.queueName,i.message,i.attemptsLeft-1,queuePublisherCallback);
            }
        }
    }

    @Override
    public void handleError(String s, JCSMPException e, long l) {

    }

    @Override
    public void responseReceived(String s) {

    }
}
