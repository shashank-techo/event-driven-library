package com.sample.solace.mqtt.listener;

import com.sample.solace.mqtt.config.MQTTClientConfig;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;

@Configuration
public class MqttQueueSubscriber implements CommandLineRunner {

  Logger logger = LoggerFactory.getLogger(MqttQueueSubscriber.class);

  @Autowired private MQTTClientConfig mqttClientConfig;

  @Override
  public void run(String... args) throws Exception {
    queueSubscriber();
  }

    /**
     * sample method for queue subscriber
     * in order to subscribe to a queue, qos should be 1(Persistent mode)
     *
     *
     * */
  public void queueSubscriber() {
    try {
      MqttClient mqttClient = mqttClientConfig.getMqttClient("SampleQueueSubscriber");
      // Callback - Anonymous inner-class for receiving messages
      mqttClient.setCallback(
          new MqttCallback() {

            public void messageArrived(String topic, MqttMessage message) throws Exception {
              // Called when a message arrives from the server that
              // matches any subscription made by the client
              String time = new Timestamp(System.currentTimeMillis()).toString();
              logger.info(
                  "Method queue subscriber \nReceived a Message!"
                      + "\n\tTime:    "
                      + time
                      + "\n\tTopic:   "
                      + topic
                      + "\n\tMessage: "
                      + new String(message.getPayload())
                      + "\n\tQoS:     "
                      + message.getQos()
                      + "\n");
            }

            public void connectionLost(Throwable cause) {
              logger.error("Connection to Solace messaging lost!" + cause.getMessage());
            }

            public void deliveryComplete(IMqttDeliveryToken token) {}
          });
      mqttClient.subscribe("my_q", 1); // first parameter is queue, and next is delivery mode

    } catch (MqttException me) {
      logger.info("reason " + me.getReasonCode());
      logger.info("msg " + me.getMessage());
      logger.info("loc " + me.getLocalizedMessage());
      logger.info("cause " + me.getCause());
      logger.info("excep " + me);
      me.printStackTrace();
    }
  }
}
