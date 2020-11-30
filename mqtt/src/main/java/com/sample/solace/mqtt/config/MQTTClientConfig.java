package com.sample.solace.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MQTTClientConfig {
    @Value("${mqttusername}")
    private String username;
    @Value("${mqtthost}")
    private String host;
    @Value("${mqttpassword}")
    private String password;

    /**
     * method to create mqttclient instance
     * @param               clientId
     *
     * @return              mqttClient
     * */
    public MqttClient getMqttClient(String clientId) throws MqttException {
        MqttClient mqttClient = new MqttClient(host, clientId);
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setAutomaticReconnect(Boolean.TRUE);
        mqttClient.connect(mqttConnectOptions);
        return mqttClient;
    }
}
