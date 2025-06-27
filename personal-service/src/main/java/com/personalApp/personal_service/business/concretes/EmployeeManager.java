package com.personalApp.personal_service.business.concretes;

import com.personalApp.personal_service.business.abstracts.CompanyService;
import com.personalApp.personal_service.business.abstracts.DepartmentService;
import com.personalApp.personal_service.business.abstracts.EmployeeService;
import com.personalApp.personal_service.business.requests.CreateEmployeeRequest;
import com.personalApp.personal_service.business.requests.UpdateEmployeeRequest;
import com.personalApp.personal_service.business.responses.GetAllEmployeeResponse;
import com.personalApp.personal_service.business.responses.GetByIdEmployeeResponse;
import com.personalApp.personal_service.core.utilities.mappers.EmployeeMapper;
import com.personalApp.personal_service.core.utilities.mappers.ModelMapperService;
import com.personalApp.personal_service.dataAccess.abstracts.EmployeeRepository;
import com.personalApp.personal_service.entities.concretes.Employee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeManager implements EmployeeService {

    private ModelMapperService modelMapperService;
    private EmployeeRepository employeeRepository;
    private DepartmentService departmentService;
    private CompanyService companyService;
    private EmployeeMapper employeeMapper;

    public List<GetAllEmployeeResponse> getAll() {

        List<Employee> employees = employeeRepository.findAll();

        return employees.stream().map(departmant -> modelMapperService.forResponse().
                        map(departmant, GetAllEmployeeResponse.class)).collect(Collectors.toList());
    }

    public GetByIdEmployeeResponse getById(int id) {
        Employee employee = employeeRepository.findById(id).orElseThrow();

        GetByIdEmployeeResponse getByIdEmployeeResponse = modelMapperService.forResponse().
                map(employee, GetByIdEmployeeResponse.class);

        return getByIdEmployeeResponse;
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

        var employee = employeeMapper.toEntity(createEmployeeRequest);

        employeeRepository.save(employee);
    }

    public void update(UpdateEmployeeRequest updateEmployeeRequest) {
        Employee employee = modelMapperService.forRequest().
                map(updateEmployeeRequest, Employee.class);

        employeeRepository.save(employee);
    }

    public void delete(int id) {

        employeeRepository.deleteById(id);
    }
}
