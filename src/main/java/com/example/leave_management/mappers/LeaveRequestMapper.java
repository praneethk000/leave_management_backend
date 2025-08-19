package com.example.leave_management.mappers;

import com.example.leave_management.dto.response.LeaveRequestResponseDto;
import com.example.leave_management.models.Employees;
import com.example.leave_management.models.LeaveRequest;
import org.springframework.stereotype.Service;

@Service
public class LeaveRequestMapper {
    public LeaveRequestResponseDto toLeaveRequestResponseDto(LeaveRequest leaveRequest, Employees employees) {
        return new LeaveRequestResponseDto(
                leaveRequest.getId(),
                leaveRequest.getEmployee().getId(),
                leaveRequest.getEmployee().getName(),
                leaveRequest.getEmployee().getEmail(),
                leaveRequest.getEmployee().getDepartment(),
                leaveRequest.getStartDate(),
                leaveRequest.getEndDate(),
                leaveRequest.getReason(),
                leaveRequest.getStatus(),
                leaveRequest.getIdempotencyKey(),
                leaveRequest.getCreatedAt()
        );
    }
}
