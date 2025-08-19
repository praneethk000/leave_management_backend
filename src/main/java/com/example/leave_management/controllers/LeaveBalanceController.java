package com.example.leave_management.controllers;


import com.example.leave_management.dto.response.LeaveBalanceResponseDto;
import com.example.leave_management.models.LeaveBalance;
import com.example.leave_management.services.leave_balance_service.LeaveBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/web/api/leaveBalance")
public class LeaveBalanceController {

    private final LeaveBalanceService leaveBalanceService;

    public LeaveBalanceController(LeaveBalanceService leaveBalanceService) {
        this.leaveBalanceService = leaveBalanceService;
    }

    @PostMapping("v1/{employeeId}/initialize")
    public ResponseEntity<LeaveBalance> initializeLeaveBalance(@PathVariable String employeeId) {
        LeaveBalance leaveBalance = leaveBalanceService.initializeLeaveBalance(employeeId);
        return ResponseEntity.status(200).body(leaveBalance);
    }

    @GetMapping("v1/getAllLeaveBalance")
    public ResponseEntity<List<LeaveBalanceResponseDto>> getAllLeaveBalance() {
        return ResponseEntity.status(200).body(leaveBalanceService.getAllLeaveBalances());
    }

    @GetMapping("v1/{employeeId}")
    public ResponseEntity<LeaveBalance> getLeaveBalance(@PathVariable String employeeId) {
        Optional<LeaveBalance> leaveBalance = leaveBalanceService.getLeaveBalanceByEmployeeId(employeeId);
        return leaveBalance.map(balance -> ResponseEntity.status(200).body(balance)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("v1/updateLeaveBalance")
    public ResponseEntity<LeaveBalance> updateLeaveBalance(@RequestBody LeaveBalance leaveBalance) {
        return ResponseEntity.status(200).body(leaveBalance);
    }

}
