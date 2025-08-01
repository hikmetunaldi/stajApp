package com.personalApp.notification_service.business.abstracts;


import com.personalApp.shared_model.events.EmployeeEvent;


public interface MailService {

    void sendEmployeeChangeNotification(EmployeeEvent employeeEvent);

}
