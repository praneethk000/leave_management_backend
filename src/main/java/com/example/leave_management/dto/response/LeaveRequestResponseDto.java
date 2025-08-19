package com.example.leave_management.dto.response;

import com.example.leave_management.models.LeaveStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LeaveRequestResponseDto(String leaveRequestid, String employeeId, String name, String email, String department, LocalDate startDate, LocalDate endDate, String reason, LeaveStatus status, String idempotencyKey, LocalDateTime createdAt) {
}
