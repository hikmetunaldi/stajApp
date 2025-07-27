package com.personalApp.notification_service.business.abstracts;

//import com.personalApp.notification_service.business.events.EmployeeEvent;


import com.personalApp.shared_model.events.EmployeeEvent;
//bura sonradan

public interface MailService {

    void sendEmployeeChangeNotification(EmployeeEvent employeeEvent);

}
