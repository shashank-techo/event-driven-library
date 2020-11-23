package com.example.microservice.listeners;

import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.TextMessage;
import com.solacesystems.jcsmp.XMLMessageListener;
import id.co.xl.techolution.eventdrivenlib.subscriber.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolaceListener implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(SolaceListener.class);

    @Autowired
    Subscriber subscriber;

    @Override
    public void run(String... args) throws Exception {
        subscribeTopicOne();
        subscribeTopicTwo();
    }

    public void subscribeTopicOne(){
        subscriber.subscribeTopic("topic1",new XMLMessageListener() {
            @Override
            public void onReceive(BytesXMLMessage msg) {
                if (msg instanceof TextMessage) {
                    logger.info("TextMessage received: {}",
                            ((TextMessage)msg).getText());
                } else {
                    logger.info("Message received.");
                }
            }
            @Override
            public void onException(JCSMPException e) {
                logger.info("Consumer received exception: {}",e);
            }
        });
    }

    public void subscribeTopicTwo(){
        subscriber.subscribeTopic("topic2",new XMLMessageListener() {
            @Override
            public void onReceive(BytesXMLMessage msg) {
                if (msg instanceof TextMessage) {
                    logger.info("TextMessage received: {}",
                            ((TextMessage)msg).getText());
                } else {
                    logger.info("Message received.");
                }
            }
            @Override
            public void onException(JCSMPException e) {
                logger.info("Consumer received exception: {}",e);
            }
        });
    }

}
