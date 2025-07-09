package com.personalApp.personal_service.business.concretes;

import com.personalApp.personal_service.business.abstracts.EmployeeService;
import com.personalApp.personal_service.business.concretes.kafka.EmployeeKafkaProducer;
import com.personalApp.personal_service.business.requests.CreateEmployeeRequest;
import com.personalApp.personal_service.business.requests.UpdateEmployeeRequest;
import com.personalApp.personal_service.business.responses.GetAllEmployeeResponse;
import com.personalApp.personal_service.business.responses.GetByIdEmployeeResponse;
import com.personalApp.personal_service.core.utilities.mappers.EmployeeMapper;
import com.personalApp.personal_service.core.utilities.mappers.ModelMapperService;
import com.personalApp.personal_service.dataAccess.abstracts.CompanyRepository;
import com.personalApp.personal_service.dataAccess.abstracts.DepartmentRepository;
import com.personalApp.personal_service.dataAccess.abstracts.EmployeeRepository;
import com.personalApp.personal_service.entities.concretes.Employee;
import com.personalApp.personal_service.helpers.exceptions.DuplicateIdentityNumberException;
import com.personalApp.personal_service.helpers.exceptions.ValidationHelper;
import com.personalApp.shared_model.enums.ChangeType;
import com.personalApp.shared_model.events.EmployeeEvent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeManager implements EmployeeService {

    private final DepartmentRepository departmentRepository;
    private final CompanyRepository companyRepository;
    private ModelMapperService modelMapperService;
    private EmployeeRepository employeeRepository;
    private EmployeeMapper employeeMapper;
    private final EmployeeKafkaProducer employeeKafkaProducer;


    public List<GetAllEmployeeResponse> getAll() {

        List<Employee> employees = employeeRepository.findAll();

        return employees.stream()
                .map(employee -> modelMapperService.forResponse()
                        .map(employee, GetAllEmployeeResponse.class))
                .sorted(Comparator.comparing(GetAllEmployeeResponse::getId))
                .toList();
    }

    public GetByIdEmployeeResponse getById(int id) {
        var employee = employeeRepository.findById(id).orElseThrow();

        return modelMapperService.forResponse().
                map(employee, GetByIdEmployeeResponse.class);
    }

    @Override
    public void add(CreateEmployeeRequest createEmployeeRequest) {
        //        Employee employee = new Employee();
//        var department = departmentService.getDepartmentById(createEmployeeRequest.getDepartmentId());
//        employee.setDepartment(department);
//
//        var company = companyService.getCompanyById(createEmployeeRequest.getCompanyId());
//        employee.setCompany(company);
//
//        setEmployeeFromRequest(employee, createEmployeeRequest);

        var department = departmentRepository.findById(createEmployeeRequest.getDepartmentId()).
                orElseThrow(() -> new RuntimeException("Department not found"));
        var company = companyRepository.findById(createEmployeeRequest.getCompanyId()).
                orElseThrow(() -> new RuntimeException("Company not found"));
        ValidationHelper.validateDepartmentBelongsToCompany(department , company);

        Optional<Employee> existing = employeeRepository
                .findByIdentityNumber(createEmployeeRequest.getIdentityNumber());
        if (existing.isPresent()) {
            // 2. Eğer varsa, özel exception fırlat
            throw new DuplicateIdentityNumberException(createEmployeeRequest.getIdentityNumber());
        }

        var employee = employeeMapper.toEntity(createEmployeeRequest);

        employeeRepository.save(employee);
    }

    public void update(UpdateEmployeeRequest updateEmployeeRequest) {
        var employee = modelMapperService.forRequest().
                map(updateEmployeeRequest, Employee.class);

        employeeRepository.save(employee);

        EmployeeEvent employeeEvent = new EmployeeEvent();

        employeeEvent.setId(employee.getId());
        employeeEvent.setIdentityNumber(employee.getIdentityNumber());
        employeeEvent.setFirstName(employee.getFirstName());
        employeeEvent.setLastName(employee.getLastName());
        employeeEvent.setEmail(employee.getEmail());
        employeeEvent.setPhoneNumber(employee.getPhoneNumber());
        employeeEvent.setAddress(employee.getAddress());
        employeeEvent.setGender(employee.getGender());
        employeeEvent.setSalary(employee.getSalary());
        employeeEvent.setPosition(employee.getPosition());
        employeeEvent.setSeniority(employee.getSeniority());
        employeeEvent.setSeniorityy(employee.getSeniorityy());
        employeeEvent.setChangeType(ChangeType.UPDATE);
        employeeEvent.setDepartmentId(employee.getDepartment().getId());
        employeeEvent.setCompanyId(employee.getCompany().getId());

        employeeKafkaProducer.sendMessage(employeeEvent);
    }

    public void delete(int id) {

        employeeRepository.deleteById(id);
    }

    @Override
    public Employee findByIdentityNumber(String identityNumber) {
        return employeeRepository.findByIdentityNumber(identityNumber)
                .orElseThrow(() -> new RuntimeException("Employee not found with identityNumber: " + identityNumber));
    }

    @Override
    public List<Employee> findByFirstNameAndLastName(String firstName, String lastName){
        return employeeRepository.findByFirstNameAndLastName(firstName, lastName).orElseThrow
                (()-> new RuntimeException("User not found wirh firstName" +firstName+ "and lastName" +lastName));
    }

}
