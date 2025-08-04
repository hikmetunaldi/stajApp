package com.personalApp.personal_service.business.concretes;

import com.personalApp.personal_service.business.requests.CreateCompanyRequest;
import com.personalApp.personal_service.business.requests.UpdateCompanyRequest;
import com.personalApp.personal_service.business.responses.GetByIdCompanyResponse;
import com.personalApp.personal_service.core.utilities.mappers.ModelMapperService;
import com.personalApp.personal_service.dataAccess.abstracts.CompanyRepository;
import com.personalApp.personal_service.entities.concretes.Company;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyManagerTest {

    @InjectMocks
    CompanyManager companyManager;
    @Mock
    CompanyRepository companyRepository;
    @Mock
    ModelMapperService modelMapperService;
    @Mock
    ModelMapper modelMapper;

    @Test
    @DisplayName("GetAll - Success: Companies successfully brought")
    void getAll() {
        when(companyRepository.findAll()).thenReturn(createDummyCompanyList());
        when(modelMapperService.forResponse()).thenReturn(new ModelMapper());

        var response = companyManager.getAll();

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(2, response.size());
        assertEquals(1, response.get(0).getId());
        assertEquals("Test Company", response.get(0).getName());

        verify(companyRepository, times(1)).findAll();
        verify(modelMapperService, times(2)).forResponse();
    }

    @Test
    @DisplayName("GetById - Success: Company found with valid ID successfully")
    void getById() {
        int companyId = 1;

        Company mockCompany = generateDummyCompany(companyId);
        GetByIdCompanyResponse expectedResponse = generateDummyGetByIdCompanyResponse(companyId);

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(mockCompany));
        when(modelMapperService.forResponse()).thenReturn(modelMapper);
        when(modelMapper.map(mockCompany, GetByIdCompanyResponse.class)).thenReturn(expectedResponse);

        GetByIdCompanyResponse actualResponse = companyManager.getById(companyId);

        assertNotNull(actualResponse);
        assertEquals(1, actualResponse.getId());
        assertEquals("Test Company", actualResponse.getName());

        verify(companyRepository, times(1)).findById(companyId);
        verify(modelMapperService, times(1)).forResponse();
        verify(modelMapper, times(1)).map(mockCompany, GetByIdCompanyResponse.class);
    }

    @Test
    @DisplayName("GetById - Should throw NoSuchElementException when company not found")
    void getById_ShouldThrowNoSuchElementException_WhenCompanyNotFound() {
        int companyId = 1;

        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> companyManager.getById(companyId));

        verify(companyRepository, times(1)).findById(companyId);
        verify(modelMapperService, never()).forResponse();
    }

    @Test
    @DisplayName("Add - Success: New company added successfully")
    void add() {
        CreateCompanyRequest request = getCreateCompanyRequest();
        Company companyToSave = getCompanyFromCreateRequest();

        when(modelMapperService.forRequest()).thenReturn(modelMapper);
        when(modelMapper.map(request, Company.class)).thenReturn(companyToSave);
        when(companyRepository.save(companyToSave)).thenReturn(companyToSave);

        assertDoesNotThrow(() -> companyManager.add(request));

        verify(modelMapperService, times(1)).forRequest();
        verify(modelMapper, times(1)).map(request, Company.class);
        verify(companyRepository, times(1)).save(companyToSave);
    }

    @Test
    @DisplayName("Update - Success: Company updated successfully")
    void update() {
        UpdateCompanyRequest request = getUpdateCompanyRequest();
        Company mappedCompany = getUpdatedCompany();

        when(modelMapperService.forRequest()).thenReturn(modelMapper);
        when(modelMapper.map(request, Company.class)).thenReturn(mappedCompany);
        when(companyRepository.save(mappedCompany)).thenReturn(mappedCompany);

        assertDoesNotThrow(() -> companyManager.update(request));

        verify(modelMapperService, times(1)).forRequest();
        verify(modelMapper, times(1)).map(request, Company.class);
        verify(companyRepository, times(1)).save(mappedCompany);
    }

    @Test
    @DisplayName("Delete - Company deleted successfully")
    void delete() {
        int companyId = 1;

        assertDoesNotThrow(() -> companyManager.delete(companyId));

        verify(companyRepository, times(1)).deleteById(companyId);
    }

    private List<Company> createDummyCompanyList() {
        List<Company> companies = new ArrayList<>();

        Company company1 = new Company();
        company1.setId(1);
        company1.setName("Test Company");
        companies.add(company1);

        Company company2 = new Company();
        company2.setId(2);
        company2.setName("Another Company");
        companies.add(company2);

        return companies;
    }

    private static Company generateDummyCompany(int companyId) {
        Company mockCompany = new Company();
        mockCompany.setId(companyId);
        mockCompany.setName("Test Company");
        return mockCompany;
    }

    private static GetByIdCompanyResponse generateDummyGetByIdCompanyResponse(int companyId) {
        GetByIdCompanyResponse expectedResponse = new GetByIdCompanyResponse();
        expectedResponse.setId(companyId);
        expectedResponse.setName("Test Company");
        return expectedResponse;
    }

    private CreateCompanyRequest getCreateCompanyRequest() {
        CreateCompanyRequest request = new CreateCompanyRequest();
        request.setName("New Company");
        return request;
    }

    private Company getCompanyFromCreateRequest() {
        Company company = new Company();
        company.setName("New Company");
        return company;
    }

    private UpdateCompanyRequest getUpdateCompanyRequest() {
        UpdateCompanyRequest request = new UpdateCompanyRequest();
        request.setId(1);
        request.setName("Updated Company");
        return request;
    }

    private Company getUpdatedCompany() {
        Company mappedCompany = new Company();
        mappedCompany.setId(1);
        mappedCompany.setName("Updated Company");
        return mappedCompany;
    }
}