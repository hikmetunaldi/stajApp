package com.personalApp.personal_service.business.concretes.kafka;

import com.personalApp.personal_service.business.abstracts.EmployeeKafkaProducerService;
import com.personalApp.shared_model.events.EmployeeEvent;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmployeeKafkaProducer implements EmployeeKafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeKafkaProducer.class);

    private final KafkaTemplate<String, EmployeeEvent> kafkaTemplate;
    private final String topicName = "employee-events";


    @Override
    public void sendMessage(EmployeeEvent employeeEvent) {
        logger.info("Sending message to Kafka topic {}: {}", topicName, employeeEvent);
        kafkaTemplate.send(topicName, employeeEvent);
    }
}
