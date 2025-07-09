package com.personalApp.notification_service.business.events;
import com.personalApp.shared_model.enums.ChangeType;
import com.personalApp.shared_model.enums.Seniority;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class EmployeeEvent implements Serializable {

    private int id;
    private String firstName;
    private String lastName;
    private String identityNumber;
    private String email;
    private String phoneNumber;
    private String address;
    private double salary;
    private String gender;
    private String position;
    private String seniority;
    private Seniority seniorityy;
    private int departmentId;
    private int companyId;

    private ChangeType changeType;
}
