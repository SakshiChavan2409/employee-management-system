package com.employeemanagement.EmployeeManagementSystem.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.employeemanagement.EmployeeManagementSystem.entities.User;
import com.employeemanagement.EmployeeManagementSystem.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class LoginController {

    private final UserRepository repo;

    public LoginController(UserRepository repo) {
        this.repo = repo;
    }

    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req, HttpSession session) {

        String username = req.get("username");
        String password = req.get("password");
        System.out.println(username + " " + password);

        User user = repo.findByUsername(username);

        if (user == null || !user.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid credentials"));
        }

       
        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", user.getRole());

        
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            session.setMaxInactiveInterval(30 * 60); 
        } else {
            session.setMaxInactiveInterval(15 * 60); 
        }

       
        HttpStatus status;
        switch (user.getRole().toUpperCase()) {
            case "ADMIN":
                status = HttpStatus.CREATED; // 201
                break;
            case "SUPERVISOR":
                status = HttpStatus.ACCEPTED; // 202
                break;
            default:
                status = HttpStatus.OK; // 200
                break;
        }

       
        return ResponseEntity.status(status).body(Map.of(
                "username", user.getUsername(),
                "role", user.getRole(),
                "sessionTimeoutSeconds", session.getMaxInactiveInterval()
        ));
    }

    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); // Ends session immediately
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    
    @GetMapping("/session")
    public ResponseEntity<?> checkSession(HttpSession session) {
        Object username = session.getAttribute("username");
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("active", false));
        }

        return ResponseEntity.ok(Map.of(
                "active", true,
                "username", username,
                "role", session.getAttribute("role"),
                "remainingTimeoutSeconds", session.getMaxInactiveInterval()
        ));
    }
}
