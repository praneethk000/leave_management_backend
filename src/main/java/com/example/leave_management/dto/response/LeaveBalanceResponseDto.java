package com.example.leave_management.dto.response;

import java.time.LocalDate;

public record LeaveBalanceResponseDto(String leaveBalanceId, String employeeId, String name, Integer totalDays, Integer usedDays, Integer pendingDays) {
}
