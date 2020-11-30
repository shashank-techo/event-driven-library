package id.co.xl.techolution.eventdrivenlib.model;

import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.DeliveryMode;

public class MessageInfo {
    public volatile boolean acked = false;
    public volatile boolean publishedSuccessfully = false;
    public BytesXMLMessage sessionIndependentMessage = null;
    public final long id;
    public String queueName;
    public String topicName;
    public String methodName;
    public DeliveryMode deliveryMode;
    public String message;
    public int attemptsLeft;

    public MessageInfo(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("Message ID: %d, PubConf: %b, PubSuccessful: %b", id, acked, publishedSuccessfully);
    }
}
