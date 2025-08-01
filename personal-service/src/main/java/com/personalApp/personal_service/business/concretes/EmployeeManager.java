package com.personalApp.personal_service.business.concretes;

import com.personalApp.personal_service.business.abstracts.EmployeeService;
import com.personalApp.personal_service.business.concretes.kafka.EmployeeKafkaProducer;
import com.personalApp.personal_service.business.requests.CreateEmployeeRequest;
import com.personalApp.personal_service.business.requests.UpdateEmployeeRequest;
import com.personalApp.personal_service.business.responses.*;
import com.personalApp.personal_service.core.utilities.mappers.EmployeeMapper;
import com.personalApp.personal_service.core.utilities.mappers.ModelMapperService;
import com.personalApp.personal_service.dataAccess.abstracts.CompanyRepository;
import com.personalApp.personal_service.dataAccess.abstracts.DepartmentRepository;
import com.personalApp.personal_service.dataAccess.abstracts.EmployeeRepository;
import com.personalApp.personal_service.entities.concretes.Company;
import com.personalApp.personal_service.entities.concretes.Department;
import com.personalApp.personal_service.entities.concretes.Employee;
import com.personalApp.personal_service.helpers.exceptions.*;
import com.personalApp.shared_model.enums.ChangeType;
import com.personalApp.shared_model.events.EmployeeEvent;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeManager implements EmployeeService {

    private final DepartmentRepository departmentRepository;
    private final CompanyRepository companyRepository;
    private final ModelMapperService modelMapperService;
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
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
        var employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id:" + id));

        return modelMapperService.forResponse().map(employee, GetByIdEmployeeResponse.class);
    }

    @Override
    public void add(CreateEmployeeRequest createEmployeeRequest) {
        var department = departmentRepository.findById(createEmployeeRequest.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        var company = companyRepository.findById(createEmployeeRequest.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));
        ValidationHelper.validateDepartmentBelongsToCompany(department, company);

        Optional<Employee> existing = employeeRepository
                .findByIdentityNumber(createEmployeeRequest.getIdentityNumber());
        if (existing.isPresent()) {
            throw new DuplicateIdentityNumberException(createEmployeeRequest.getIdentityNumber());
        }

        var employee = employeeMapper.toEntity(createEmployeeRequest);

        employee.setDepartment(department);
        employee.setCompany(company);


        employeeRepository.save(employee);
    }


    public void update(UpdateEmployeeRequest updateEmployeeRequest) {

        Employee existingEmployee = employeeRepository.findById(updateEmployeeRequest.getId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (updateEmployeeRequest.getFirstName() != null && !updateEmployeeRequest.getFirstName().isBlank())
            existingEmployee.setFirstName(updateEmployeeRequest.getFirstName());

        if (updateEmployeeRequest.getLastName() != null && !updateEmployeeRequest.getLastName().isBlank())
            existingEmployee.setLastName(updateEmployeeRequest.getLastName());

        if (updateEmployeeRequest.getIdentityNumber() != null && !updateEmployeeRequest.getIdentityNumber().isBlank())
            existingEmployee.setIdentityNumber(updateEmployeeRequest.getIdentityNumber());

        if (updateEmployeeRequest.getEmail() != null && !updateEmployeeRequest.getEmail().isBlank())
            existingEmployee.setEmail(updateEmployeeRequest.getEmail());

        if (updateEmployeeRequest.getPhoneNumber() != null && !updateEmployeeRequest.getPhoneNumber().isBlank())
            existingEmployee.setPhoneNumber(updateEmployeeRequest.getPhoneNumber());

        if (updateEmployeeRequest.getAddress() != null && !updateEmployeeRequest.getAddress().isBlank())
            existingEmployee.setAddress(updateEmployeeRequest.getAddress());

        if (updateEmployeeRequest.getGender() != null && !updateEmployeeRequest.getGender().isBlank())
            existingEmployee.setGender(updateEmployeeRequest.getGender());

        if (updateEmployeeRequest.getSalary() != null)
            existingEmployee.setSalary(updateEmployeeRequest.getSalary());

        if (updateEmployeeRequest.getPosition() != null && !updateEmployeeRequest.getPosition().isBlank())
            existingEmployee.setPosition(updateEmployeeRequest.getPosition());

        if (updateEmployeeRequest.getSeniority() != null)
            existingEmployee.setSeniority(updateEmployeeRequest.getSeniority());

        if (updateEmployeeRequest.getDepartmentId() != null && updateEmployeeRequest.getDepartmentId() > 0) {
            Department dept = departmentRepository.findById(updateEmployeeRequest.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found."));
            existingEmployee.setDepartment(dept);
        }

        if (updateEmployeeRequest.getCompanyId() != null && updateEmployeeRequest.getCompanyId() > 0) {
            Company comp = companyRepository.findById(updateEmployeeRequest.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Company not found."));
            existingEmployee.setCompany(comp);
        }


        employeeRepository.save(existingEmployee);

        EmployeeEvent employeeEvent = new EmployeeEvent();
        employeeEvent.setId(existingEmployee.getId());
        employeeEvent.setIdentityNumber(existingEmployee.getIdentityNumber());
        employeeEvent.setFirstName(existingEmployee.getFirstName());
        employeeEvent.setLastName(existingEmployee.getLastName());
        employeeEvent.setEmail(existingEmployee.getEmail());
        employeeEvent.setPhoneNumber(existingEmployee.getPhoneNumber());
        employeeEvent.setAddress(existingEmployee.getAddress());
        employeeEvent.setGender(existingEmployee.getGender());
        employeeEvent.setSalary(existingEmployee.getSalary());
        employeeEvent.setPosition(existingEmployee.getPosition());
        employeeEvent.setSeniority(existingEmployee.getSeniority());
        employeeEvent.setChangeType(ChangeType.UPDATE);
        employeeEvent.setDepartmentId(existingEmployee.getDepartment().getId());
        employeeEvent.setCompanyId(existingEmployee.getCompany().getId());

        employeeKafkaProducer.sendMessage(employeeEvent);
    }

    public void delete(int id) {

        employeeRepository.deleteById(id);
    }

    @Override
    public FindEmployeesByIdentityNumber findByIdentityNumber(String identityNumber) {
        var employee = employeeRepository.findByIdentityNumber(identityNumber)
                .orElseThrow(() -> new RuntimeException("Employee not found with identity number: " + identityNumber));

        return modelMapperService.forResponse().map(employee, FindEmployeesByIdentityNumber.class);
    }


    @Override
    public List<FindEmployeesByDepartmentResponse> findEmployeesByDepartmentId(int departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));

        List<Employee> employees = employeeRepository.findEmployeesByDepartment(department);

        if (employees.isEmpty()) {
            throw new NoEmployeesInDepartmentException(departmentId);
        }

        ModelMapper modelMapper = modelMapperService.forResponse();
        return employees.stream().
                map(employee -> modelMapper.map(employee, FindEmployeesByDepartmentResponse.class)).
                collect(Collectors.toList());
    }

    @Override
    public List<FindEmployeesByCompanyResponse> findEmployeesByCompanyId(int companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new CompanyNotFoundException(companyId));

        List<Employee> employees = employeeRepository.findEmployeesByCompany(company);

        if(employees.isEmpty()){
            throw new NoEmployeesInCompanyException(companyId);
        }

        ModelMapper modelMapper = modelMapperService.forResponse();
        return employees.stream()
                .map(employee -> modelMapper.map(employee,FindEmployeesByCompanyResponse.class))
                .collect(Collectors.toList());
    }


    @Override
    public List<FindEmployeesByFirstNameAndLastName> findByFirstNameAndLastName(String firstName, String lastName) {
        List<Employee> employees = employeeRepository.findByFirstNameAndLastName(firstName,lastName)
                .orElseThrow(()-> new RuntimeException("Employee not found with firstname:" +firstName+ "and lastname" +lastName));

        return employees.stream()
                .map(employee->modelMapperService.forResponse().map(employee, FindEmployeesByFirstNameAndLastName.class))
                .collect(Collectors.toList());
    }

}
