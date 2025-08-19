package com.example.leave_management.mappers;

import com.example.leave_management.models.Employees;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class EmployeeMapper {
    public Employees ToEmployee(String id, String name, String department, String email, LocalDate joiningDate){
        return Employees.builder().id(id).name(name).department(department).email(email).joiningDate(joiningDate).build();
    }
}
