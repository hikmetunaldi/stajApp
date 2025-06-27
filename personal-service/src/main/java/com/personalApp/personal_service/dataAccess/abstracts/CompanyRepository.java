package com.personalApp.personal_service.dataAccess.abstracts;

import com.personalApp.personal_service.entities.concretes.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
}
