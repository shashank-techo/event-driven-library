package com.example.microservice.listeners;

import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.TextMessage;
import com.solacesystems.jcsmp.XMLMessageListener;
import id.co.xl.techolution.eventdrivenlib.subscriber.QueueSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolaceQueueListener implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(SolaceQueueListener.class);


    @Autowired
    QueueSubscriber queueSubscriber;

    /**
     * subscriber to a queue running in a background thread
     *
     * */
    @Override
    public void run(String... args) throws Exception {
        try {
            queueSubscriber.queueListener("my_q", new XMLMessageListener() {
                @Override
                public void onReceive(BytesXMLMessage msg) {
                    if (msg instanceof TextMessage) {
                        logger.info("Queue TextMessage received: {}", ((TextMessage) msg).getText());
                    } else {
                        logger.info("Message received.");
                    }
                    msg.ackMessage();
                }
                @Override
                public void onException(JCSMPException e) {
                    logger.info("Consumer received exception: {}", e);
                }
            });
        } catch (JCSMPException e) {
            e.printStackTrace();
        }
    }
}
