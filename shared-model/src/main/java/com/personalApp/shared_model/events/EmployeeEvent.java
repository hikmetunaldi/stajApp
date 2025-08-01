package com.personalApp.shared_model.events;


import com.personalApp.shared_model.enums.ChangeType;
import com.personalApp.shared_model.enums.Seniority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeEvent {
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
    private Seniority seniority;
    private ChangeType changeType;
    private int departmentId;
    private int companyId;


}
