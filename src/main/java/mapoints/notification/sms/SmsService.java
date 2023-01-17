package mapoints.notification.sms;

import java.io.IOException;
import java.util.Collection;

public interface SmsService {
    void send(String recipient, String message);

    void send(Collection<String> recipients, String message) throws IOException;
}
