package com.personalApp.personal_service.dataAccess.abstracts;

import com.personalApp.personal_service.entities.concretes.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
