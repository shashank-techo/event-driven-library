package com.example.microservice.controller;


import id.co.xl.techolution.eventdrivenlib.publisher.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    Logger logger = LoggerFactory.getLogger(SampleController.class);


    @Autowired
    private Publisher publisher;


    @GetMapping(value="/default-api")
    public ResponseEntity<String> defaultMethod()
    {
        publisher.publishToTopic("topic1","Hello topic 1");
        publisher.publishToTopic("topic2","Hello topic 2");

        return new ResponseEntity<>("Done", HttpStatus.OK);
    }


}
