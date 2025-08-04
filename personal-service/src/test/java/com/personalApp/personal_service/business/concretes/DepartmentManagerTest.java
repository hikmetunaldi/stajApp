package com.personalApp.personal_service.business.concretes;

import com.personalApp.personal_service.business.requests.CreateDepartmentRequest;
import com.personalApp.personal_service.business.requests.UpdateDepartmentRequest;
import com.personalApp.personal_service.business.responses.GetByIdDepartmentResponse;
import com.personalApp.personal_service.core.utilities.mappers.DepartmentMapper;
import com.personalApp.personal_service.core.utilities.mappers.ModelMapperService;
import com.personalApp.personal_service.dataAccess.abstracts.CompanyRepository;
import com.personalApp.personal_service.dataAccess.abstracts.DepartmentRepository;
import com.personalApp.personal_service.entities.concretes.Company;
import com.personalApp.personal_service.entities.concretes.Department;
import com.personalApp.personal_service.helpers.exceptions.DepartmentNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentManagerTest {

    @InjectMocks
    DepartmentManager departmentManager;
    @Mock
    DepartmentRepository departmentRepository;
    @Mock
    CompanyRepository companyRepository;
    @Mock
    ModelMapperService modelMapperService;
    @Mock
    ModelMapper modelMapper;
    @Mock
    DepartmentMapper departmentMapper;

    @Test
    @DisplayName("GetAll - Success: Departments successfully brought")
    void getAll() {
        List<Department> mockDepartments = createDummyDepartmentList();

        when(departmentRepository.findAll()).thenReturn(mockDepartments);
        when(modelMapperService.forResponse()).thenReturn(new ModelMapper());

        var response = departmentManager.getAll();

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(2, response.size());
        assertEquals(1, response.get(0).getId());
        assertEquals("IT Department", response.get(0).getName());
        assertEquals(2, response.get(1).getId());
        assertEquals("HR Department", response.get(1).getName());

        verify(departmentRepository, times(1)).findAll();
        verify(modelMapperService, times(2)).forResponse();
    }

    @Test
    @DisplayName("GetAll - Success: Returns empty list when no departments exist")
    void getAll_ShouldReturnEmptyList_WhenNoDepartmentsExist() {
        when(departmentRepository.findAll()).thenReturn(new ArrayList<>());

        var response = departmentManager.getAll();

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(departmentRepository, times(1)).findAll();
        verify(modelMapperService, never()).forResponse();
    }

    @Test
    @DisplayName("GetById - Success: Department found with valid ID successfully")
    void getById() {
        int departmentId = 1;
        Department mockDepartment = generateDummyDepartment(departmentId);
        GetByIdDepartmentResponse expectedResponse = generateDummyGetByIdDepartmentResponse(departmentId);

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(mockDepartment));
        when(modelMapperService.forResponse()).thenReturn(modelMapper);
        when(modelMapper.map(mockDepartment, GetByIdDepartmentResponse.class)).thenReturn(expectedResponse);

        GetByIdDepartmentResponse actualResponse = departmentManager.getById(departmentId);

        assertNotNull(actualResponse);
        assertEquals(1, actualResponse.getId());
        assertEquals("IT Department", actualResponse.getName());

        verify(departmentRepository, times(1)).findById(departmentId);
        verify(modelMapperService, times(1)).forResponse();
        verify(modelMapper, times(1)).map(mockDepartment, GetByIdDepartmentResponse.class);
    }

    @Test
    @DisplayName("GetById - Should throw DepartmentNotFoundException when department not found")
    void getById_ShouldThrowDepartmentNotFoundException_WhenDepartmentNotFound() {
        int departmentId = 999;

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        DepartmentNotFoundException exception = assertThrows(
                DepartmentNotFoundException.class,
                () -> departmentManager.getById(departmentId)
        );

        assertNotNull(exception);

        verify(departmentRepository, times(1)).findById(departmentId);
        verify(modelMapperService, never()).forResponse();
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    @DisplayName("Add - Success: New department added successfully")
    void add() {
        CreateDepartmentRequest request = getCreateDepartmentRequest();
        Company mockCompany = getCompany();
        Department departmentToSave = getDepartmentToSave(mockCompany);

        when(companyRepository.findById(request.getCompanyId())).thenReturn(Optional.of(mockCompany));
        when(departmentMapper.toEntity(request)).thenReturn(departmentToSave);
        when(departmentRepository.save(departmentToSave)).thenReturn(departmentToSave);

        assertDoesNotThrow(() -> departmentManager.add(request));

        verify(companyRepository, times(1)).findById(request.getCompanyId());
        verify(departmentMapper, times(1)).toEntity(request);
        verify(departmentRepository, times(1)).save(departmentToSave);
    }

    @Test
    @DisplayName("Add - Should throw RuntimeException when company not found")
    void add_ShouldThrowRuntimeException_WhenCompanyNotFound() {
        CreateDepartmentRequest request = getCreateDepartmentRequest();

        when(companyRepository.findById(request.getCompanyId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> departmentManager.add(request)
        );

        assertEquals("Company not found", exception.getMessage());

        verify(companyRepository, times(1)).findById(request.getCompanyId());
        verify(departmentMapper, never()).toEntity(any());
        verify(departmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Update - Success: Department updated successfully")
    void update() {
        UpdateDepartmentRequest request = getUpdateDepartmentRequest();
        Department mappedDepartment = getUpdatedDepartment();

        when(modelMapperService.forRequest()).thenReturn(modelMapper);
        when(modelMapper.map(request, Department.class)).thenReturn(mappedDepartment);
        when(departmentRepository.save(mappedDepartment)).thenReturn(mappedDepartment);

        assertDoesNotThrow(() -> departmentManager.update(request));

        verify(modelMapperService, times(1)).forRequest();
        verify(modelMapper, times(1)).map(request, Department.class);
        verify(departmentRepository, times(1)).save(mappedDepartment);
    }

    @Test
    @DisplayName("Delete - Department deleted successfully")
    void delete() {
        int departmentId = 1;

        assertDoesNotThrow(() -> departmentManager.delete(departmentId));

        verify(departmentRepository, times(1)).deleteById(departmentId);
    }

    private List<Department> createDummyDepartmentList() {
        List<Department> departments = new ArrayList<>();

        Department department1 = new Department();
        department1.setId(1);
        department1.setName("IT Department");
        departments.add(department1);

        Department department2 = new Department();
        department2.setId(2);
        department2.setName("HR Department");
        departments.add(department2);

        return departments;
    }

    private static Department generateDummyDepartment(int departmentId) {
        Department mockDepartment = new Department();
        mockDepartment.setId(departmentId);
        mockDepartment.setName("IT Department");
        return mockDepartment;
    }

    private static GetByIdDepartmentResponse generateDummyGetByIdDepartmentResponse(int departmentId) {
        GetByIdDepartmentResponse expectedResponse = new GetByIdDepartmentResponse();
        expectedResponse.setId(departmentId);
        expectedResponse.setName("IT Department");
        return expectedResponse;
    }

    private CreateDepartmentRequest getCreateDepartmentRequest() {
        CreateDepartmentRequest request = new CreateDepartmentRequest();
        request.setName("HR Department");
        request.setCompanyId(1);
        return request;
    }

    private Company getCompany() {
        Company mockCompany = new Company();
        mockCompany.setId(1);
        mockCompany.setName("Test Company");
        return mockCompany;
    }

    private Department getDepartmentToSave(Company company) {
        Department departmentToSave = new Department();
        departmentToSave.setName("HR Department");
        departmentToSave.setCompany(company);
        return departmentToSave;
    }

    private UpdateDepartmentRequest getUpdateDepartmentRequest() {
        UpdateDepartmentRequest request = new UpdateDepartmentRequest();
        request.setId(1);
        request.setName("Updated HR Department");
        request.setCompanyId(1);
        return request;
    }

    private Department getUpdatedDepartment() {
        Department mappedDepartment = new Department();
        mappedDepartment.setId(1);
        mappedDepartment.setName("Updated HR Department");
        return mappedDepartment;
    }
}