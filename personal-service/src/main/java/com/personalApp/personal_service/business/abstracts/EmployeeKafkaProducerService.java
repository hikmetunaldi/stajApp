package com.personalApp.personal_service.business.abstracts;

import com.personalApp.shared_model.events.EmployeeEvent;

public interface EmployeeKafkaProducerService {
    void sendMessage(EmployeeEvent employeeEvent);
}
