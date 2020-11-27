package com.example.microservice.controller;


import com.solacesystems.jcsmp.DeliveryMode;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPStreamingPublishEventHandler;
import id.co.xl.techolution.eventdrivenlib.publisher.Publisher;
import id.co.xl.techolution.eventdrivenlib.publisher.QueuePublisher;
import id.co.xl.techolution.eventdrivenlib.subscriber.QueueSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class SampleController {

    Logger logger = LoggerFactory.getLogger(SampleController.class);


    @Autowired
    private Publisher publisher;

    @Autowired
    private QueuePublisher queuePublisher;

    @GetMapping(value="/default-api")
    public ResponseEntity<String> defaultMethod()
    {

        // publishing message to topic along with a callback handler
        publisher.publishToTopic("topic1", DeliveryMode.DIRECT,"Hello topic 1", new JCSMPStreamingPublishEventHandler() {
            @Override
            public void handleError(String s, JCSMPException e, long l) {
                logger.info("exception occurred : "+s);
            }
            @Override
            public void responseReceived(String s) {
                logger.info("response received : "+s);
            }
        });
        //publishing message to a queue
        queuePublisher.publishToQueue("my_q", Arrays.asList("demo messgae"));
        return new ResponseEntity<>("Done", HttpStatus.OK);
    }


}
