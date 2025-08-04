package com.personalApp.notification_service.business.concretes.mail;

import com.personalApp.notification_service.business.abstracts.EmployeeFeignClient;
import com.personalApp.notification_service.business.abstracts.MailService;
import com.personalApp.shared_model.events.EmployeeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SmtpMailService implements MailService {

    private static final Logger logger = LoggerFactory.getLogger(SmtpMailService.class);

    private final JavaMailSender mailSender;
    private final EmployeeFeignClient employeeFeignClient;

    public SmtpMailService(JavaMailSender mailSender, EmployeeFeignClient employeeFeignClient) {
        this.mailSender = mailSender;
        this.employeeFeignClient = employeeFeignClient;
    }

    @Override
    public void sendEmployeeChangeNotification(EmployeeEvent event) {
        try {
            logger.info("Fetching employee details for ID: {} using Feign Client", event.getId());
            EmployeeEvent fullEmployeeData = employeeFeignClient.getEmployeeById(event.getId());

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(fullEmployeeData.getEmail());
            message.setSubject("Personel Bilgilerinizde Değişiklik Yapıldı");
            message.setText(buildEmailBody(fullEmployeeData, event.getChangeType().toString()));

            mailSender.send(message);
            logger.info("Notification mail sent successfully to {} for employee ID: {}",
                    fullEmployeeData.getEmail(), fullEmployeeData.getId());

        } catch (feign.FeignException e) {
            logger.error("Feign Client error while fetching employee with ID {}: Status: {}, Message: {}",
                    event.getId(), e.status(), e.getMessage());
            sendFallbackNotification(event);

        } catch (Exception e) {
            logger.error("Failed to send mail for employee ID {}: {}", event.getId(), e.getMessage(), e);
            sendFallbackNotification(event);
        }
    }

    private void sendFallbackNotification(EmployeeEvent event) {
        try {
            logger.info("Sending fallback notification using event data for employee ID: {}", event.getId());

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(event.getEmail());
            message.setSubject("Personel Bilgilerinizde Değişiklik Yapıldı");
            message.setText(buildEmailBody(event, event.getChangeType().toString()));

            mailSender.send(message);
            logger.info("Fallback notification mail sent to {}", event.getEmail());

        } catch (Exception e) {
            logger.error("Failed to send fallback mail to {}: {}", event.getEmail(), e.getMessage(), e);
        }
    }

    private String buildEmailBody(EmployeeEvent employeeData, String changeType) {
        return String.format(
                "Sayın %s %s,\n\n" +
                        "%s işlemi gerçekleştirilmiştir.\n\n" +
                        "Personel ID: %d\n" +
                        "Email: %s\n\n" +
                        "Detaylar için insan kaynakları departmanı ile iletişime geçebilirsiniz.\n\n" +
                        "İyi günler.",
                employeeData.getFirstName(),
                employeeData.getLastName(),
                changeType,
                employeeData.getId(),
                employeeData.getEmail()
        );
    }
}