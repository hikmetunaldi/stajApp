package com.personalApp.personal_service.business.concretes;

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
import com.personalApp.shared_model.enums.Gender;
import com.personalApp.shared_model.enums.Seniority;
import com.personalApp.shared_model.events.EmployeeEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeManagerTest {

    @InjectMocks
    private EmployeeManager employeeManager;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private ModelMapperService modelMapperService;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private EmployeeKafkaProducer employeeKafkaProducer;
    @Mock
    private ModelMapper mockModelMapper;

    private Employee testEmployee;
    private Employee testEmployee2;
    private Department testDepartment;
    private Company testCompany;
    private GetAllEmployeeResponse getAllResponse1;
    private GetAllEmployeeResponse getAllResponse2;
    private GetByIdEmployeeResponse getByIdResponse;
    private CreateEmployeeRequest createRequest;
    private UpdateEmployeeRequest updateRequest;

    @BeforeEach
    void setUp() {
        setupTestData();
    }


    @Test
    @DisplayName("GetAll - Should return empty list when no employees exist")
    void getAll_ShouldReturnEmptyList_WhenNoEmployeesExist() {
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        List<GetAllEmployeeResponse> result = employeeManager.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(employeeRepository, times(1)).findAll();
        verifyNoInteractions(modelMapperService);
    }

    @Test
    @DisplayName("GetById - Should throw RuntimeException with correct message when employee not found")
    void getById_ShouldThrowRuntimeExceptionWithCorrectMessage_WhenEmployeeNotFound() {
        int nonExistentId = 999;

        when(employeeRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> employeeManager.getById(nonExistentId));

        assertEquals("Employee with id: 999 not found", exception.getMessage());

        verify(employeeRepository, times(1)).findById(nonExistentId);
        verifyNoInteractions(modelMapperService);
    }

    @Test
    @DisplayName("GetById - Should handle zero and negative IDs")
    void getById_ShouldHandleInvalidIds() {
        when(employeeRepository.findById(0)).thenReturn(Optional.empty());

        RuntimeException exception1 = assertThrows(RuntimeException.class,
                () -> employeeManager.getById(0));
        assertEquals("Employee with id: 0 not found", exception1.getMessage());

        when(employeeRepository.findById(-1)).thenReturn(Optional.empty());

        RuntimeException exception2 = assertThrows(RuntimeException.class,
                () -> employeeManager.getById(-1));
        assertEquals("Employee with id: -1 not found", exception2.getMessage());
    }

    @Test
    @DisplayName("Add - Should save employee with correct data when valid request provided")
    void add_ShouldSaveEmployeeWithCorrectData_WhenValidRequestProvided() {
        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);

        when(departmentRepository.findById(createRequest.getDepartmentId())).thenReturn(Optional.of(testDepartment));
        when(companyRepository.findById(createRequest.getCompanyId())).thenReturn(Optional.of(testCompany));
        when(employeeRepository.findByIdentityNumber(createRequest.getIdentityNumber())).thenReturn(Optional.empty());
        when(employeeMapper.toEntity(createRequest)).thenReturn(testEmployee);
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        assertDoesNotThrow(() -> employeeManager.add(createRequest));

        verify(employeeRepository).save(employeeCaptor.capture());
        Employee savedEmployee = employeeCaptor.getValue();

        assertNotNull(savedEmployee);
        assertEquals(testDepartment, savedEmployee.getDepartment());
        assertEquals(testCompany, savedEmployee.getCompany());

        verify(departmentRepository, times(1)).findById(createRequest.getDepartmentId());
        verify(companyRepository, times(1)).findById(createRequest.getCompanyId());
        verify(employeeRepository, times(1)).findByIdentityNumber(createRequest.getIdentityNumber());
        verify(employeeMapper, times(1)).toEntity(createRequest);
    }

    @Test
    @DisplayName("Add - Should throw RuntimeException with correct message when department not found")
    void add_ShouldThrowRuntimeExceptionWithCorrectMessage_WhenDepartmentNotFound() {
        when(departmentRepository.findById(createRequest.getDepartmentId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> employeeManager.add(createRequest));

        assertEquals("Department not found with id: 1", exception.getMessage());

        verify(departmentRepository, times(1)).findById(createRequest.getDepartmentId());
        verifyNoInteractions(companyRepository);
        verifyNoInteractions(employeeRepository);
    }

    @Test
    @DisplayName("Add - Should throw RuntimeException with correct message when company not found")
    void add_ShouldThrowRuntimeExceptionWithCorrectMessage_WhenCompanyNotFound() {
        when(departmentRepository.findById(createRequest.getDepartmentId())).thenReturn(Optional.of(testDepartment));
        when(companyRepository.findById(createRequest.getCompanyId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> employeeManager.add(createRequest));

        assertEquals("Company not found with id:1", exception.getMessage());

        verify(departmentRepository, times(1)).findById(createRequest.getDepartmentId());
        verify(companyRepository, times(1)).findById(createRequest.getCompanyId());
        verifyNoInteractions(employeeRepository);
    }

    @Test
    @DisplayName("Add - Should throw DuplicateIdentityNumberException when identity number exists")
    void add_ShouldThrowDuplicateIdentityNumberException_WhenIdentityNumberExists() {
        when(departmentRepository.findById(createRequest.getDepartmentId())).thenReturn(Optional.of(testDepartment));
        when(companyRepository.findById(createRequest.getCompanyId())).thenReturn(Optional.of(testCompany));
        when(employeeRepository.findByIdentityNumber(createRequest.getIdentityNumber())).thenReturn(Optional.of(testEmployee));

        DuplicateIdentityNumberException exception = assertThrows(DuplicateIdentityNumberException.class,
                () -> employeeManager.add(createRequest));

        assertNotNull(exception);

        verify(employeeRepository, times(1)).findByIdentityNumber(createRequest.getIdentityNumber());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Update - Should update employee and send Kafka message when valid request provided")
    void update_ShouldUpdateEmployeeAndSendKafkaMessage_WhenValidRequestProvided() {
        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
        ArgumentCaptor<EmployeeEvent> eventCaptor = ArgumentCaptor.forClass(EmployeeEvent.class);

        when(employeeRepository.findById(updateRequest.getId())).thenReturn(Optional.of(testEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);
        doNothing().when(employeeMapper).updateEmployee(updateRequest, testEmployee);
        doNothing().when(employeeKafkaProducer).sendMessage(any(EmployeeEvent.class));

        assertDoesNotThrow(() -> employeeManager.update(updateRequest));

        verify(employeeRepository).save(employeeCaptor.capture());
        verify(employeeKafkaProducer).sendMessage(eventCaptor.capture());

        Employee savedEmployee = employeeCaptor.getValue();
        EmployeeEvent sentEvent = eventCaptor.getValue();

        assertEquals(testEmployee, savedEmployee);
        assertNotNull(sentEvent);
        assertEquals(testEmployee.getId(), sentEvent.getId());
        assertEquals(testEmployee.getIdentityNumber(), sentEvent.getIdentityNumber());
        assertEquals(testEmployee.getFirstName(), sentEvent.getFirstName());
        assertEquals(ChangeType.UPDATE, sentEvent.getChangeType());
        assertEquals(testEmployee.getDepartment().getId(), sentEvent.getDepartmentId());
        assertEquals(testEmployee.getCompany().getId(), sentEvent.getCompanyId());

        verify(employeeRepository, times(1)).findById(updateRequest.getId());
        verify(employeeMapper, times(1)).updateEmployee(updateRequest, testEmployee);
    }

    @Test
    @DisplayName("Update - Should throw RuntimeException with correct message when employee not found")
    void update_ShouldThrowRuntimeExceptionWithCorrectMessage_WhenEmployeeNotFound() {
        when(employeeRepository.findById(updateRequest.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> employeeManager.update(updateRequest));

        assertEquals("Employee with id: 1 not found", exception.getMessage());

        verify(employeeRepository, times(1)).findById(updateRequest.getId());
        verifyNoInteractions(employeeMapper);
        verify(employeeRepository, never()).save(any());
        verifyNoInteractions(employeeKafkaProducer);
    }

    @Test
    @DisplayName("Delete - Should call repository deleteById when valid ID provided")
    void delete_ShouldCallRepositoryDeleteById_WhenValidIdProvided() {
        int employeeId = 1;

        doNothing().when(employeeRepository).deleteById(employeeId);

        assertDoesNotThrow(() -> employeeManager.delete(employeeId));

        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

    @Test
    @DisplayName("FindByIdentityNumber - Should return complete employee data when identity number exists")
    void findByIdentityNumber_ShouldReturnCompleteEmployeeData_WhenIdentityNumberExists() {
        String identityNumber = "12345678901";
        FindEmployeesByIdentityNumber expectedResponse = createFindEmployeesByIdentityNumberResponse();

        when(employeeRepository.findByIdentityNumber(identityNumber)).thenReturn(Optional.of(testEmployee));
        when(employeeMapper.toFindEmployeesByIdentityNumber(testEmployee)).thenReturn(expectedResponse);

        FindEmployeesByIdentityNumber result = employeeManager.findByIdentityNumber(identityNumber);

        assertNotNull(result);
        assertEquals(expectedResponse.getId(), result.getId());
        assertEquals(expectedResponse.getFirstName(), result.getFirstName());
        assertEquals(expectedResponse.getLastName(), result.getLastName());
        assertEquals(expectedResponse.getIdentityNumber(), result.getIdentityNumber());

        verify(employeeRepository, times(1)).findByIdentityNumber(identityNumber);
        verify(employeeMapper, times(1)).toFindEmployeesByIdentityNumber(testEmployee);
    }

    @Test
    @DisplayName("FindByIdentityNumber - Should throw EmployeeIdentityNotFoundException when not found")
    void findByIdentityNumber_ShouldThrowEmployeeIdentityNotFoundException_WhenNotFound() {
        String identityNumber = "99999999999";

        when(employeeRepository.findByIdentityNumber(identityNumber)).thenReturn(Optional.empty());

        EmployeeIdentityNotFoundException exception = assertThrows(EmployeeIdentityNotFoundException.class,
                () -> employeeManager.findByIdentityNumber(identityNumber));

        assertNotNull(exception);

        verify(employeeRepository, times(1)).findByIdentityNumber(identityNumber);
        verifyNoInteractions(employeeMapper);
    }

    @Test
    @DisplayName("FindByIdentityNumber - Should handle null and empty identity number")
    void findByIdentityNumber_ShouldHandleNullAndEmptyIdentityNumber() {
        when(employeeRepository.findByIdentityNumber(null)).thenReturn(Optional.empty());

        assertThrows(EmployeeIdentityNotFoundException.class,
                () -> employeeManager.findByIdentityNumber(null));

        when(employeeRepository.findByIdentityNumber("")).thenReturn(Optional.empty());

        assertThrows(EmployeeIdentityNotFoundException.class,
                () -> employeeManager.findByIdentityNumber(""));
    }

    @Test
    @DisplayName("FindEmployeesByDepartmentId - Should throw DepartmentNotFoundException when department not found")
    void findEmployeesByDepartmentId_ShouldThrowDepartmentNotFoundException_WhenDepartmentNotFound() {
        int departmentId = 999;

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        DepartmentNotFoundException exception = assertThrows(DepartmentNotFoundException.class,
                () -> employeeManager.findEmployeesByDepartmentId(departmentId));

        assertNotNull(exception);

        verify(departmentRepository, times(1)).findById(departmentId);
        verifyNoInteractions(employeeRepository);
    }

    @Test
    @DisplayName("FindEmployeesByDepartmentId - Should throw NoEmployeesInDepartmentException when no employees")
    void findEmployeesByDepartmentId_ShouldThrowNoEmployeesInDepartmentException_WhenNoEmployees() {
        int departmentId = 1;

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(testDepartment));
        when(employeeRepository.findEmployeesByDepartment(testDepartment)).thenReturn(Collections.emptyList());

        NoEmployeesInDepartmentException exception = assertThrows(NoEmployeesInDepartmentException.class,
                () -> employeeManager.findEmployeesByDepartmentId(departmentId));

        assertNotNull(exception);

        verify(departmentRepository, times(1)).findById(departmentId);
        verify(employeeRepository, times(1)).findEmployeesByDepartment(testDepartment);
    }

    private void setupTestData() {
        testCompany = new Company();
        testCompany.setId(1);
        testCompany.setName("Test Company");

        testDepartment = new Department();
        testDepartment.setId(1);
        testDepartment.setName("IT Department");
        testDepartment.setCompany(testCompany);

        testEmployee = new Employee();
        testEmployee.setId(1);
        testEmployee.setFirstName("Hikmet");
        testEmployee.setLastName("Ünaldı");
        testEmployee.setIdentityNumber("12345678901");
        testEmployee.setEmail("hikmet@gmail.com");
        testEmployee.setPhoneNumber("5536200289");
        testEmployee.setAddress("İstanbul, Türkiye");
        testEmployee.setGender(Gender.ERKEK);
        testEmployee.setSalary(50000);
        testEmployee.setPosition("Developer");
        testEmployee.setSeniority(Seniority.fromValue(0));
        testEmployee.setDepartment(testDepartment);
        testEmployee.setCompany(testCompany);

        testEmployee2 = new Employee();
        testEmployee2.setId(2);
        testEmployee2.setFirstName("Gamze");
        testEmployee2.setLastName("Kaya");
        testEmployee2.setIdentityNumber("98765432109");
        testEmployee2.setEmail("gamze@gmail.com");
        testEmployee2.setPhoneNumber("5073201659");
        testEmployee2.setAddress("Ankara, Türkiye");
        testEmployee2.setGender(Gender.KADIN);
        testEmployee2.setSalary(60000);
        testEmployee2.setPosition("Senior Developer");
        testEmployee2.setSeniority(Seniority.fromValue(2));
        testEmployee2.setDepartment(testDepartment);
        testEmployee2.setCompany(testCompany);

        getAllResponse1 = new GetAllEmployeeResponse();
        getAllResponse1.setId(1);
        getAllResponse1.setFirstName("Hikmet");
        getAllResponse1.setLastName("Ünaldı");
        getAllResponse1.setEmail("hikmet@gmail.com");
        getAllResponse1.setPosition("Developer");

        getAllResponse2 = new GetAllEmployeeResponse();
        getAllResponse2.setId(2);
        getAllResponse2.setFirstName("Gamze");
        getAllResponse2.setLastName("Kaya");
        getAllResponse2.setEmail("gamze@gmail.com");
        getAllResponse2.setPosition("Senior Developer");

        getByIdResponse = new GetByIdEmployeeResponse();
        getByIdResponse.setId(1);
        getByIdResponse.setFirstName("Hikmet");
        getByIdResponse.setLastName("Ünaldı");
        getByIdResponse.setEmail("hikmet@gmail.com");
        getByIdResponse.setIdentityNumber("12345678901");
        getByIdResponse.setPhoneNumber("5536200289");
        getByIdResponse.setAddress("İstanbul, Türkiye");
        getByIdResponse.setPosition("Developer");

        createRequest = new CreateEmployeeRequest();
        createRequest.setFirstName("Gamze");
        createRequest.setLastName("Metin");
        createRequest.setIdentityNumber("98765432109");
        createRequest.setEmail("gamze@gmail.com");
        createRequest.setDepartmentId(1);
        createRequest.setCompanyId(1);

        updateRequest = new UpdateEmployeeRequest();
        updateRequest.setId(1);
        updateRequest.setFirstName("Hikmet Güncellenmiş");
        updateRequest.setLastName("Ünaldı Güncellenmiş");
        updateRequest.setEmail("hikmet.updated@gmail.com");
    }

    private FindEmployeesByIdentityNumber createFindEmployeesByIdentityNumberResponse() {
        FindEmployeesByIdentityNumber response = new FindEmployeesByIdentityNumber();
        response.setId(1);
        response.setFirstName("Hikmet");
        response.setLastName("Ünaldı");
        response.setIdentityNumber("12345678901");
        response.setEmail("hikmet@gmail.com");
        return response;
    }

    private FindEmployeesByDepartmentResponse createFindEmployeesByDepartmentResponse(Employee employee) {
        FindEmployeesByDepartmentResponse response = new FindEmployeesByDepartmentResponse();
        response.setId(employee.getId());
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setEmail(employee.getEmail());
        response.setPosition(employee.getPosition());
        return response;
    }
}