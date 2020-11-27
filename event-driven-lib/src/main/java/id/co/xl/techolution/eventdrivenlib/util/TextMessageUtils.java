package id.co.xl.techolution.eventdrivenlib.util;

import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.TextMessage;

public class TextMessageUtils {

    public static TextMessage getTextMessageInstance(){
        return JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
    }

}
