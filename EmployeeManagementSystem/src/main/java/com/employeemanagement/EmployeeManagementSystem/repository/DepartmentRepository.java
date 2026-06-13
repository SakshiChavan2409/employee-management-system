package com.employeemanagement.EmployeeManagementSystem.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.employeemanagement.EmployeeManagementSystem.entities.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

}