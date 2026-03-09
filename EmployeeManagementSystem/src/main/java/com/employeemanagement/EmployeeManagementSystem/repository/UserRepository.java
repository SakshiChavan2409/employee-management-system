package com.employeemanagement.EmployeeManagementSystem.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.employeemanagement.EmployeeManagementSystem.entities.User;
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
