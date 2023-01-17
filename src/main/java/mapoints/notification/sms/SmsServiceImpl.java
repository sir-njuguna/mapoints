package mapoints.notification.sms;


import com.africastalking.AfricasTalking;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
class SmsServiceImpl implements SmsService {
    private String africasTalkingUsername;
    private String africasTalkingApiKey;
    private String africasTalkingSenderID;

    public void send(String recipient, String message) {
        List<String> recipients = new ArrayList<>();
        recipients.add(recipient);
        this.send(recipients, message);
    }

    public void send(Collection<String> recipients, String message) {
        String[] recipientsArray = recipients.toArray(new String[0]);
        AfricasTalking.initialize(africasTalkingUsername, africasTalkingApiKey);
        com.africastalking.SmsService smsService = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        Runnable runnable = () -> {
            try {
                smsService.send(message, africasTalkingSenderID, recipientsArray, false);
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        };
        (new Thread(runnable)).start();
    }

    @Value("${africastalking.username}")
    public void setAfricasTalkingUsername(String africasTalkingUsername) {
        this.africasTalkingUsername = africasTalkingUsername;
    }

    @Value("${africastalking.api-key}")
    public void setAfricasTalkingApiKey(String africasTalkingApiKey) {
        this.africasTalkingApiKey = africasTalkingApiKey;
    }

    @Value("${africastalking.sender-id}")
    public void setAfricasTalkingSenderID(String africasTalkingSenderID) {
        this.africasTalkingSenderID = africasTalkingSenderID;
    }
}
