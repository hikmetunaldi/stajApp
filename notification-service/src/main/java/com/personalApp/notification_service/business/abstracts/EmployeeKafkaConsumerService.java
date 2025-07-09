package com.personalApp.notification_service.business.abstracts;

import com.personalApp.notification_service.business.events.EmployeeEvent;

public interface EmployeeKafkaConsumerService {
    void consumeEmployeeEvent(EmployeeEvent employeeEvent);
}
