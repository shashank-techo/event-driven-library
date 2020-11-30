package com.example.microservice.controller;


import com.example.microservice.callbacks.TopicPublisherCallback;
import com.solacesystems.jcsmp.DeliveryMode;
import com.solacesystems.jcsmp.JCSMPException;
import id.co.xl.techolution.eventdrivenlib.publisher.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TopicController {

    Logger logger = LoggerFactory.getLogger(TopicController.class);


    @Autowired
    private Publisher publisher;

    @Autowired
    ApplicationContext applicationContext;

    @GetMapping(value="/default-api/topic")
    public ResponseEntity<String> topicMethod() throws JCSMPException
    {

        // publishing message to topic along with a callback handler
        TopicPublisherCallback topicPublisherCallback = applicationContext.getBean(TopicPublisherCallback.class);
        publisher.publishToTopic("topic1", DeliveryMode.DIRECT,"Hello topic 1",1, topicPublisherCallback);
        return new ResponseEntity<>("Done", HttpStatus.OK);
    }




}
