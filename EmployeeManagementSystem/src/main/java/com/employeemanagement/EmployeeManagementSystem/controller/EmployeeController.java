package com.employeemanagement.EmployeeManagementSystem.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.employeemanagement.EmployeeManagementSystem.entities.Employee;
import com.employeemanagement.EmployeeManagementSystem.entities.User;
import com.employeemanagement.EmployeeManagementSystem.repository.EmployeeRepository;
import com.employeemanagement.EmployeeManagementSystem.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class EmployeeController {

    private final EmployeeRepository repo;
    private final UserRepository repoUser;

    public EmployeeController(EmployeeRepository repo,UserRepository repoUser) {
        this.repo = repo;
        this.repoUser=repoUser;
    }
    

    @GetMapping
    public ResponseEntity<?> getAll(HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Please log in first"));
        }

        if (role.equalsIgnoreCase("EMPLOYEE")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Access denied"));
        }

        List<Employee> employees = repo.findAll();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getEmployeeByUsername(@PathVariable String username, HttpSession session) {

        String role = (String) session.getAttribute("role");

        if (role == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Please login first"));
        }

        User user = repoUser.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found"));
        }

        Map<String, Object> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("role", user.getRole());
        data.put("message", "Employee data loaded");

        return ResponseEntity.ok(data);
    }
    
    @PostMapping
    public ResponseEntity<?> addEmployee(@RequestBody Employee emp, HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Login required"));
        }

        
        if (role.equalsIgnoreCase("EMPLOYEE") || role.equalsIgnoreCase("ADMIN")) {
            emp.setId(0); // 👈 ensure Hibernate treats this as a NEW record
            Employee saved = repo.save(emp);
            User user1 = new User();
            user1.setUsername(emp.getEmail());
            user1.setPassword("12345");
            user1.setRole("EMPLOYEE");
            repoUser.save(user1);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Not authorized"));
    }


   
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable int id, @RequestBody Employee updatedEmp, HttpSession session) {
        String role = (String) session.getAttribute("role");

        if (role == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Login required"));
        }

        if (!(role.equalsIgnoreCase("SUPERVISOR") || role.equalsIgnoreCase("ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Not authorized to update employees"));
        }

        Employee emp = repo.findById(id).orElse(null);
        if (emp == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Employee not found"));
        }

        emp.setName(updatedEmp.getName());
        emp.setEmail(updatedEmp.getEmail());
        emp.setDepartment(updatedEmp.getDepartment());
        emp.setSalary(updatedEmp.getSalary());

        Employee saved = repo.save(emp);
        return ResponseEntity.ok(saved);
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable int id, HttpSession session) {
        String role = (String) session.getAttribute("role");

        if (role == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Login required"));
        }

        if (!role.equalsIgnoreCase("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Only Admin can delete employees"));
        }

        if (!repo.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Employee not found"));
        }

        repo.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Employee deleted successfully"));
    }
} 