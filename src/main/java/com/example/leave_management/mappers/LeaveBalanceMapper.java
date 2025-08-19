package com.example.leave_management.mappers;

import com.example.leave_management.dto.response.LeaveBalanceResponseDto;
import com.example.leave_management.models.Employees;
import com.example.leave_management.models.LeaveBalance;
import org.springframework.stereotype.Service;

@Service
public class LeaveBalanceMapper {
    public LeaveBalanceResponseDto toLeaveBalanceResponseDto(LeaveBalance leaveBalance, Employees employees) {
        return new LeaveBalanceResponseDto(
                leaveBalance.getId(),
                leaveBalance.getEmployee().getId(),
                leaveBalance.getEmployee().getName(),
                leaveBalance.getTotalDays(),
                leaveBalance.getUsedDays(),
                leaveBalance.getPendingDays()
        );
    }
}
