package com.example.leave_management.controllers;

import com.example.leave_management.dto.request.CreateEmployeeRequestDto;
import com.example.leave_management.models.Employees;
import com.example.leave_management.services.employees_service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/web/api/employee")
public class EmployeeController {
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("v1/createEmployee")
    public ResponseEntity<String> createEmployee(@RequestBody CreateEmployeeRequestDto createEmployeeRequestDto){
        return ResponseEntity.status(200).body(employeeService.createEmployee(createEmployeeRequestDto.name(), createEmployeeRequestDto.email(), createEmployeeRequestDto.department(), createEmployeeRequestDto.joiningDate()));
    }

    @GetMapping("v1/getEmployeeById")
    public ResponseEntity<Optional<Employees>> getEmployeeById(@RequestParam("employeeId") String employeeId){
        return ResponseEntity.status(200).body(employeeService.getEmployeeById(employeeId));
    }

    @GetMapping("v1/getAllEmployees")
    public ResponseEntity<List<Employees>> getAllEmployees(){
        return ResponseEntity.status(200).body(employeeService.getAllEmployees());
    }

    @DeleteMapping("v1/deleteEmployeeById/{employeeId}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String employeeId){
        return ResponseEntity.status(200).body(employeeService.deleteEmployeeById(employeeId));
    }
}
