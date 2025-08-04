package com.personalApp.notification_service.business.abstracts;


import com.personalApp.notification_service.config.FeignClientConfig;
import com.personalApp.shared_model.events.EmployeeEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employee-service", configuration = FeignClientConfig.class)
public interface EmployeeFeignClient {

    @GetMapping("/api/employees/{id}")
    EmployeeEvent getEmployeeById(@PathVariable("id") int id);
}
