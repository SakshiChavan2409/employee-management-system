package com.employeemanagement.EmployeeManagementSystem.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.employeemanagement.EmployeeManagementSystem.entities.Department;
import com.employeemanagement.EmployeeManagementSystem.repository.DepartmentRepository;


@RestController
@RequestMapping("/api/department")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping
    public List<Department> getAllDepartments(){
        return departmentRepository.findAll();
    }

    @PostMapping
    public Department addDepartment(@RequestBody Department department){
        return departmentRepository.save(department);
    }
    
    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable int id){
        departmentRepository.deleteById(id);
    }

}