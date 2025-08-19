package com.example.leave_management.services.employees_service;

import com.example.leave_management.models.Employees;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public interface EmployeeService {
    String createEmployee(String name, String email, String department, LocalDate joiningDate);
    String deleteEmployeeById(String id);
    List<Employees> getAllEmployees();
    Optional<Employees> getEmployeeById(String id);
}
