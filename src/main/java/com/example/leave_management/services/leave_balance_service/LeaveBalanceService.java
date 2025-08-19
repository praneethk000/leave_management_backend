package com.example.leave_management.services.leave_balance_service;

import com.example.leave_management.dto.response.LeaveBalanceResponseDto;
import com.example.leave_management.models.LeaveBalance;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public interface LeaveBalanceService {
    LeaveBalance initializeLeaveBalance(String id);
    Optional<LeaveBalance> getLeaveBalanceByEmployeeId(String id);
    String updateLeaveBalance(LeaveBalance leaveBalance);
    List<LeaveBalanceResponseDto> getAllLeaveBalances();
}
