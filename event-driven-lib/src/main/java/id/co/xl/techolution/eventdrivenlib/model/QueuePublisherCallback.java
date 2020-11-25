package id.co.xl.techolution.eventdrivenlib.model;

import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPStreamingPublishCorrelatingEventHandler;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

@Data
public class QueuePublisherCallback implements JCSMPStreamingPublishCorrelatingEventHandler {

    Logger logger = LoggerFactory.getLogger(QueuePublisherCallback.class);

//    private CountDownLatch countDownLatch;

    @Override
    public void handleErrorEx(Object key, JCSMPException cause, long timestamp) {
        if (key instanceof MessageInfo) {
            MessageInfo i = (MessageInfo) key;
            i.acked = true;
            logger.info("Queue error received for {}, error was {} ", i, cause);
        }
//        this.countDownLatch.countDown();
    }

    @Override
    public void responseReceivedEx(Object key) {
        if (key instanceof MessageInfo) {
            MessageInfo i = (MessageInfo) key;
            i.acked = true;
            i.publishedSuccessfully = true;
            logger.info("Queue acknowledgement received for {} ", i);
        }
//        this.countDownLatch.countDown();
    }

    @Override
    public void handleError(String messageID, JCSMPException cause, long timestamp) {
    }

    @Override
    public void responseReceived(String messageID) {
    }
}
