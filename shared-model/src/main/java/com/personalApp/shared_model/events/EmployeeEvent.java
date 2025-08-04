package com.personalApp.shared_model.events;


import com.personalApp.shared_model.enums.ChangeType;
import com.personalApp.shared_model.enums.Gender;
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
    private Integer id;
    private String firstName;
    private String lastName;
    private String identityNumber;
    private String email;
    private String phoneNumber;
    private String address;
    private Double salary;
    private Gender gender;
    private String position;
    private Seniority seniority;
    private ChangeType changeType;
    private Integer departmentId;
    private Integer companyId;
}
