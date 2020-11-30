package com.example.microservice.controller;

import com.example.microservice.callbacks.QueuePublisherCallback;
import com.solacesystems.jcsmp.JCSMPException;
import id.co.xl.techolution.eventdrivenlib.publisher.QueuePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;


@RestController
public class QueueController {

    Logger logger = LoggerFactory.getLogger(QueueController.class);


    @Autowired
    private QueuePublisher queuePublisher;

    @Autowired
    ApplicationContext applicationContext;

    @GetMapping(value="/default-api/queue")
    public ResponseEntity<String> queueMethod() throws JCSMPException
    {
        QueuePublisherCallback queuePublisherCallback = applicationContext.getBean(QueuePublisherCallback.class);
        queuePublisher.publishToQueue("queue", Arrays.asList("demo messgaenalskndlakndsneiaofdeaeaeafadad","eafancoianxoainsi8he8f9afafaeffffffffffa"),1,queuePublisherCallback);
        return new ResponseEntity<>("Done", HttpStatus.OK);
    }
}
