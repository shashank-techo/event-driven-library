package com.example.microservice.callbacks;

import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPStreamingPublishCorrelatingEventHandler;
import com.solacesystems.jcsmp.TextMessage;
import id.co.xl.techolution.eventdrivenlib.model.MessageInfo;
import id.co.xl.techolution.eventdrivenlib.publisher.Publisher;
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
public class TopicPublisherCallback implements JCSMPStreamingPublishCorrelatingEventHandler {

    Logger logger = LoggerFactory.getLogger(TopicPublisherCallback.class);

    @Autowired
    Publisher publisher;

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
                TopicPublisherCallback topicPublisherCallback = applicationContext.getBean(TopicPublisherCallback.class);
                publisher.publishToTopic(i.queueName, i.deliveryMode,i.message,i.attemptsLeft-1, topicPublisherCallback);

//              publisher.publishToTopic(i.queueName, (TextMessage) i.sessionIndependentMessage,i.attemptsLeft-1, topicPublisherCallback);
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
