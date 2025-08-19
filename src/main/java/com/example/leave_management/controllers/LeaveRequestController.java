package com.example.leave_management.controllers;


import com.example.leave_management.dto.request.ApplyLeaveRequestRequestDto;
import com.example.leave_management.dto.response.LeaveRequestResponseDto;
import com.example.leave_management.models.LeaveRequest;
import com.example.leave_management.services.leave_request_service.LeaveRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/web/api/leaveRequest")
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    @GetMapping("v1/getAllLeaveRequest")
    public ResponseEntity<List<LeaveRequestResponseDto>> getAllLeaveRequest() {
        return ResponseEntity.status(200).body(leaveRequestService.getAllLeaveRequests());
    }

    @PostMapping("v1/apply")
    public ResponseEntity<String> applyForLeaveRequest(@RequestBody ApplyLeaveRequestRequestDto applyLeaveRequestRequestDto) {
        String response = leaveRequestService.applyForLeave(applyLeaveRequestRequestDto.employeeId(), applyLeaveRequestRequestDto.startDate(), applyLeaveRequestRequestDto.endDate(), applyLeaveRequestRequestDto.reason(), applyLeaveRequestRequestDto.idempotencyKey());
        return ResponseEntity.ok(response);
    }

    @PutMapping("v1/{requestId}/status")
    public ResponseEntity<String> updateLeaveStatus(@PathVariable String requestId, @RequestParam("status") String status) {
        String response = leaveRequestService.updateLeaveStatus(requestId, status);
        return ResponseEntity.ok(response);
    }

}
