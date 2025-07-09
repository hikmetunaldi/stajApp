package com.personalApp.notification_service.business.concretes.mail;

import com.personalApp.notification_service.business.abstracts.MailService;
import com.personalApp.notification_service.business.events.EmployeeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.invoker.cache.CachingOperationInvokerAdvisor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class SmtpMailService implements MailService {

    private static final Logger logger = LoggerFactory.getLogger(SmtpMailService.class);

    private final JavaMailSender mailSender;

    public SmtpMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmployeeChangeNotification(EmployeeEvent event) {
        // Basit bir mail mesajı örneği
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getEmail());
        message.setSubject("Personel Bilgilerinizde Değişiklik Yapıldı");
        message.setText(buildEmailBody(event));

        try {
            mailSender.send(message);
            logger.info("Notification mail sent to {}", event.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send mail to {}: {}", event.getEmail(), e.getMessage());
        }
    }

    private String buildEmailBody(EmployeeEvent event) {
        return String.format(
                "Dear %s %s,\n\n has been made to your personnel information.\n\n Best regards.",
                event.getFirstName(), event.getLastName(), event.getChangeType()
        );
    }
}
