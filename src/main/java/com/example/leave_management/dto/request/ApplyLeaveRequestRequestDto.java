package com.example.leave_management.dto.request;

import java.time.LocalDate;

public record ApplyLeaveRequestRequestDto(String employeeId, LocalDate startDate, LocalDate endDate, String reason, String idempotencyKey) {
}
