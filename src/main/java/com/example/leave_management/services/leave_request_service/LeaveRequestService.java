package com.example.leave_management.services.leave_request_service;

import com.example.leave_management.dto.response.LeaveRequestResponseDto;
import com.example.leave_management.models.LeaveRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface LeaveRequestService {
    String applyForLeave(String id, LocalDate startDate, LocalDate endDate, String reason, String idempotencyKey);
    String updateLeaveStatus(String id, String status);
    List<LeaveRequestResponseDto> getAllLeaveRequests();
}
