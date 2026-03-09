package com.employeemanagement.EmployeeManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.employeemanagement.EmployeeManagementSystem.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
