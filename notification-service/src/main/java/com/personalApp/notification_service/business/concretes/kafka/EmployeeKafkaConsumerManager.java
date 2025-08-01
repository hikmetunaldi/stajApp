
package com.personalApp.notification_service.business.concretes.kafka;

import com.personalApp.notification_service.business.abstracts.EmployeeKafkaConsumerService;
import com.personalApp.notification_service.business.abstracts.MailService;
import com.personalApp.shared_model.events.EmployeeEvent;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmployeeKafkaConsumerManager implements EmployeeKafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeKafkaConsumerManager.class);

    private final MailService mailService;

    @KafkaListener(
            topics = "employee-events",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )

    @Override
    public void consumeEmployeeEvent(EmployeeEvent employeeEvent) {
        System.out.println("Listener tetiklendi, gelen event: " + employeeEvent);
        logger.info("Received EmployeeEvent from Kafka: {}", employeeEvent);
        mailService.sendEmployeeChangeNotification(employeeEvent);
    }

}
