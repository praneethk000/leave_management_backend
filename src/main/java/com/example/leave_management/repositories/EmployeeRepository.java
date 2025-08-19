package com.example.leave_management.repositories;

import com.example.leave_management.models.Employees;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employees, String> {

}
