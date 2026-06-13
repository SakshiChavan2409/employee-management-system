package com.employeemanagement.EmployeeManagementSystem.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.employeemanagement.EmployeeManagementSystem.entities.User;
import com.employeemanagement.EmployeeManagementSystem.repository.UserRepository;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    // =============================
    // ADMIN DASHBOARD
    // =============================
    @GetMapping("/admin")
    public ResponseEntity<?> getAdminDashboard() {

        Map<String, Object> data = new HashMap<>();
        data.put("totalUsers", userRepository.count());
        data.put("totalEmployees", userRepository.findAll()
                .stream().filter(u -> u.getRole().equals("EMPLOYEE")).count());
        data.put("totalSupervisors", userRepository.findAll()
                .stream().filter(u -> u.getRole().equals("SUPERVISOR")).count());

        return ResponseEntity.ok(data);
    }

    // =============================
    // GET ALL USERS
    // =============================
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // =============================
    // ADD USER
    // =============================
    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // =============================
    // DELETE USER
    // =============================
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {

        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}