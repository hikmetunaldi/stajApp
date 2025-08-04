package com.personalApp.personal_service.business.concretes;

import com.personalApp.personal_service.business.requests.CreateDepartmentRequest;
import com.personalApp.personal_service.business.requests.UpdateDepartmentRequest;
import com.personalApp.personal_service.business.responses.GetAllDepartmentResponse;
import com.personalApp.personal_service.business.responses.GetByIdDepartmentResponse;
import com.personalApp.personal_service.core.utilities.mappers.DepartmentMapper;
import com.personalApp.personal_service.dataAccess.abstracts.CompanyRepository;
import com.personalApp.personal_service.dataAccess.abstracts.DepartmentRepository;
import com.personalApp.personal_service.entities.concretes.Company;
import com.personalApp.personal_service.entities.concretes.Department;
import com.personalApp.personal_service.helpers.exceptions.CompanyNotFoundException;
import com.personalApp.personal_service.helpers.exceptions.DepartmentNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    DepartmentMapper departmentMapper;

    @Test
    @DisplayName("GetAll - Success: Departments successfully brought")
    void getAll() {
        List<Department> mockDepartments = createDummyDepartmentList();
        List<GetAllDepartmentResponse> mockResponses = createDummyGetAllDepartmentResponseList();

        when(departmentRepository.findAll()).thenReturn(mockDepartments);
        when(departmentMapper.toGetAllDepartmentResponse(mockDepartments.get(0))).thenReturn(mockResponses.get(0));
        when(departmentMapper.toGetAllDepartmentResponse(mockDepartments.get(1))).thenReturn(mockResponses.get(1));

        var response = departmentManager.getAll();

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(2, response.size());
        assertEquals(1, response.get(0).getId());
        assertEquals("IT Department", response.get(0).getName());
        assertEquals(2, response.get(1).getId());
        assertEquals("HR Department", response.get(1).getName());

        verify(departmentRepository, times(1)).findAll();
        verify(departmentMapper, times(2)).toGetAllDepartmentResponse(any(Department.class));
    }

    @Test
    @DisplayName("GetAll - Success: Returns empty list when no departments exist")
    void getAll_ShouldReturnEmptyList_WhenNoDepartmentsExist() {
        when(departmentRepository.findAll()).thenReturn(new ArrayList<>());

        var response = departmentManager.getAll();

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(departmentRepository, times(1)).findAll();
        verify(departmentMapper, never()).toGetAllDepartmentResponse(any(Department.class));
    }

    @Test
    @DisplayName("GetById - Success: Department found with valid ID successfully")
    void getById() {
        int departmentId = 1;
        Department mockDepartment = generateDummyDepartment(departmentId);
        GetByIdDepartmentResponse expectedResponse = generateDummyGetByIdDepartmentResponse(departmentId);

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(mockDepartment));
        when(departmentMapper.toGetByIdDepartmentResponse(mockDepartment)).thenReturn(expectedResponse);

        GetByIdDepartmentResponse actualResponse = departmentManager.getById(departmentId);

        assertNotNull(actualResponse);
        assertEquals(1, actualResponse.getId());
        assertEquals("IT Department", actualResponse.getName());

        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentMapper, times(1)).toGetByIdDepartmentResponse(mockDepartment);
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
        verify(departmentMapper, never()).toGetByIdDepartmentResponse(any(Department.class));
    }

    @Test
    @DisplayName("Add - Success: New department added successfully")
    void add() {
        CreateDepartmentRequest request = getCreateDepartmentRequest();
        Company mockCompany = getCompany();
        Department departmentToSave = getDepartmentToSave(mockCompany);

        when(companyRepository.findById(request.getCompanyId())).thenReturn(Optional.of(mockCompany));
        when(departmentMapper.toEntity(request)).thenReturn(departmentToSave);
        when(departmentRepository.save(any(Department.class))).thenReturn(departmentToSave);

        assertDoesNotThrow(() -> departmentManager.add(request));

        verify(companyRepository, times(1)).findById(request.getCompanyId());
        verify(departmentMapper, times(1)).toEntity(request);
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    @DisplayName("Add - Should throw CompanyNotFoundException when company not found")
    void add_ShouldThrowCompanyNotFoundException_WhenCompanyNotFound() {
        CreateDepartmentRequest request = getCreateDepartmentRequest();

        when(companyRepository.findById(request.getCompanyId())).thenReturn(Optional.empty());

        CompanyNotFoundException exception = assertThrows(
                CompanyNotFoundException.class,
                () -> departmentManager.add(request)
        );

        assertEquals("Company not found with id:" + request.getCompanyId(), exception.getMessage());

        verify(companyRepository, times(1)).findById(request.getCompanyId());
        verify(departmentMapper, never()).toEntity(any());
        verify(departmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Update - Success: Department updated successfully")
    void update() {
        UpdateDepartmentRequest request = getUpdateDepartmentRequest();
        Department existingDepartment = getUpdatedDepartment();

        when(departmentRepository.findById(request.getId())).thenReturn(Optional.of(existingDepartment));
        doNothing().when(departmentMapper).updateDepartment(request, existingDepartment);
        when(departmentRepository.save(existingDepartment)).thenReturn(existingDepartment);

        assertDoesNotThrow(() -> departmentManager.update(request));

        verify(departmentRepository, times(1)).findById(request.getId());
        verify(departmentMapper, times(1)).updateDepartment(request, existingDepartment);
        verify(departmentRepository, times(1)).save(existingDepartment);
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

    private List<GetAllDepartmentResponse> createDummyGetAllDepartmentResponseList() {
        List<GetAllDepartmentResponse> responses = new ArrayList<>();

        GetAllDepartmentResponse response1 = new GetAllDepartmentResponse();
        response1.setId(1);
        response1.setName("IT Department");
        responses.add(response1);

        GetAllDepartmentResponse response2 = new GetAllDepartmentResponse();
        response2.setId(2);
        response2.setName("HR Department");
        responses.add(response2);

        return responses;
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