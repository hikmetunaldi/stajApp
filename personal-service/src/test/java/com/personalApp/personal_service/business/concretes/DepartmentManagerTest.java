package com.personalApp.personal_service.business.concretes;

import com.personalApp.personal_service.core.utilities.mappers.ModelMapperService;
import com.personalApp.personal_service.dataAccess.abstracts.DepartmentRepository;
import com.personalApp.personal_service.entities.concretes.Department;
import com.personalApp.personal_service.entities.concretes.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentManagerTest {
    @InjectMocks
    DepartmentManager departmentManager;
    @Mock
    DepartmentRepository departmentRepository;
    @Mock
    ModelMapperService modelMapperService;

    @Test
    void getAll() {
        when(departmentRepository.findAll()).thenReturn(dummyCreateEmployeeRequest());
        when(modelMapperService.forResponse()).thenReturn(new ModelMapper());

        var response = departmentManager.getAll();

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.get(0).getId());
    }

    @Test
    void getById() {
    }

    @Test
    void add() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void getDepartmentById() {
    }

    private List<Department> dummyCreateEmployeeRequest() {
        List<Department> departments = new ArrayList<>();
        Department department = new Department();
        department.setId(1);
        department.setName("Department 1");
        departments.add(department);
        return departments;
    }
}