package com.sample.solace.mqtt.service;

import com.sample.solace.mqtt.config.MQTTClientConfig;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueueService {

    Logger logger = LoggerFactory.getLogger(QueueService.class);

    @Autowired
    private MQTTClientConfig mqttClientConfig;

    public void publishToQueue(){
        try {
            MqttClient mqttClient = mqttClientConfig.getMqttClient("SampleQueuePublisher");
            String content = "MQTT queue 1 from SampleQueuePublisher";
            MqttMessage message = new MqttMessage(content.getBytes());
            // Here we are using QoS of 1 (equivalent to Persistent Messages in Solace)
            message.setQos(1);
            logger.info("Publishing message: " + content);
            mqttClient.publish("my_q", message);
            mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
