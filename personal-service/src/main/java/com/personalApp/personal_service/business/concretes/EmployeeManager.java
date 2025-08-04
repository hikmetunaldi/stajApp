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
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeKafkaProducer employeeKafkaProducer;

    public List<GetAllEmployeeResponse> getAll() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream()
                .map(employeeMapper::toGetAllEmployeeResponse)
                .sorted(Comparator.comparing(GetAllEmployeeResponse::getId))
                .toList();
    }

    public GetByIdEmployeeResponse getById(int id) {
        var employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundByIdException(id));

        return employeeMapper.toGetByIdEmployeeResponse(employee);
    }

    @Override
    public void add(CreateEmployeeRequest createEmployeeRequest) {
        var department = departmentRepository.findById(createEmployeeRequest.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(createEmployeeRequest.getDepartmentId()));
        var company = companyRepository.findById(createEmployeeRequest.getCompanyId())
                .orElseThrow(() -> new CompanyNotFoundException(createEmployeeRequest.getCompanyId()));
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
                .orElseThrow(() -> new EmployeeNotFoundByIdException(updateEmployeeRequest.getId()));

        employeeMapper.updateEmployee(updateEmployeeRequest, existingEmployee);
        employeeRepository.save(existingEmployee);
        EmployeeEvent employeeEvent = createEmployeeEvent(existingEmployee);

        employeeKafkaProducer.sendMessage(employeeEvent);
    }

    public void delete(int id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public FindEmployeesByIdentityNumber findByIdentityNumber(String identityNumber) {
        Employee employee = employeeRepository.findByIdentityNumber(identityNumber)
                .orElseThrow(() -> new EmployeeIdentityNotFoundException(identityNumber));

        return employeeMapper.toFindEmployeesByIdentityNumber(employee);
    }

    @Override
    public List<FindEmployeesByDepartmentResponse> findEmployeesByDepartmentId(int departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));

        List<Employee> employees = employeeRepository.findEmployeesByDepartment(department);

        if (employees.isEmpty()) {
            throw new NoEmployeesInDepartmentException(departmentId);
        }

        return employees.stream()
                .map(employeeMapper::toFindEmployeesByDepartmentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<FindEmployeesByCompanyResponse> findEmployeesByCompanyId(int companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new CompanyNotFoundException(companyId));

        List<Employee> employees = employeeRepository.findEmployeesByCompany(company);

        if(employees.isEmpty()){
            throw new NoEmployeesInCompanyException(companyId);
        }

        return employees.stream()
                .map(employeeMapper::toFindEmployeesByCompanyResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<FindEmployeesByCompanyIdAndDepartmentId> findEmployeesByCompanyIdAndDepartmentId(int companyId, int departmentId) {
        companyRepository.findById(companyId)
                .orElseThrow(()-> new CompanyNotFoundException(companyId));
        departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));

        List<Employee> employees = employeeRepository.findByCompanyIdAndDepartmentId(companyId, departmentId);

        if(employees.isEmpty()){
            throw new EmployeeNotFoundByCompanyAndDepartmentException(companyId, departmentId);
        }

        return employees.stream()
                .map(employeeMapper::toFindEmployeesByCompanyIdAndDepartmentId)
                .collect(Collectors.toList());
    }

    @Override
    public List<FindEmployeesByFirstName> findEmployeesByFirstName(String firstName){
        List<Employee> employees = employeeRepository.findEmployeesByFirstName(firstName);

        if(employees.isEmpty()){
            throw new EmployeeNotFoundByFirstNameException(firstName);
        }

        return employees.stream()
                .map(employeeMapper::toFindEmployeesByFirstName)
                .collect(Collectors.toList());
    }

    @Override
    public List<FindEmployeesByLastName> findEmployeesByLastName(String lastName){
        List<Employee> employees = employeeRepository.findEmployeesByLastName(lastName);

        if (employees.isEmpty()){
            throw new EmployeeNotFoundByLastNameException(lastName);
        }

        return employees.stream()
                .map(employeeMapper::toFindEmployeesByLastName)
                .collect(Collectors.toList());
    }

    @Override
    public List<FindEmployeesByFirstNameAndLastName> findByFirstNameAndLastName(String firstName, String lastName) {
        List<Employee> employees = employeeRepository.findByFirstNameAndLastName(firstName,lastName);

        if(employees.isEmpty()){
            throw new EmployeeNotFoundByFirstNameAndLastNameException(firstName, lastName);
        }

        return employees.stream()
                .map(employeeMapper::toFindEmployeesByFirstNameAndLastName)
                .collect(Collectors.toList());
    }

    private static EmployeeEvent createEmployeeEvent(Employee existingEmployee) {
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
        return employeeEvent;
    }
}