package com.example.leave_management.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateEmployeeRequestDto(String name, String email, String department, LocalDate joiningDate, LocalDateTime createdAt) {
}
