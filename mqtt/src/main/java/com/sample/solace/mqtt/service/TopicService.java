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
public class TopicService {

    Logger logger = LoggerFactory.getLogger(TopicService.class);

    @Autowired
    private MQTTClientConfig mqttClientConfig;

    public void publishToTopic(){
        try {
            MqttClient mqttClient = mqttClientConfig.getMqttClient("SampleTopictPublisher");
            String content = "MQTT topic 1 SampleClient1";
            MqttMessage message = new MqttMessage(content.getBytes());
            //QoS of 0 (equivalent to Direct Messaging in Solace)
            message.setQos(0);
            logger.info("Publishing message: " + content);
            mqttClient.publish("sample/topic1", message);
            mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
