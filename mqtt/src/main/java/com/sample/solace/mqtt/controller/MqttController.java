package com.sample.solace.mqtt.controller;

import com.sample.solace.mqtt.service.QueueService;
import com.sample.solace.mqtt.service.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MqttController {

    Logger logger = LoggerFactory.getLogger(MqttController.class);

    @Autowired
    TopicService topicService;

    @Autowired
    QueueService queueService;

    @GetMapping(value="/default-api")
    public ResponseEntity<String> defaultMethod()
    {
        topicService.publishToTopic();
        queueService.publishToQueue();
        return new ResponseEntity<>("Done", HttpStatus.OK);
    }
}
