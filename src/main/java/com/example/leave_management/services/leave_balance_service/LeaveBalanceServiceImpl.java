package com.example.leave_management.services.leave_balance_service;

import com.example.leave_management.dto.response.LeaveBalanceResponseDto;
import com.example.leave_management.mappers.LeaveBalanceMapper;
import com.example.leave_management.models.Employees;
import com.example.leave_management.models.LeaveBalance;
import com.example.leave_management.models.LeaveRequest;
import com.example.leave_management.models.LeaveStatus;
import com.example.leave_management.repositories.EmployeeRepository;
import com.example.leave_management.repositories.LeaveBalanceRepository;
import com.example.leave_management.repositories.LeaveRequestRepository;
import com.example.leave_management.utils.UUIDService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.time.temporal.ChronoUnit;

@Service
public class LeaveBalanceServiceImpl implements LeaveBalanceService {

    private final LeaveBalanceMapper leaveBalanceMapper;
    private final UUIDService uuidService;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    public LeaveBalanceServiceImpl(UUIDService uuidService, LeaveBalanceRepository leaveBalanceRepository,
                                   EmployeeRepository employeeRepository, LeaveBalanceMapper leaveBalanceMapper,
                                   LeaveRequestRepository leaveRequestRepository) {
        this.uuidService = uuidService;
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.employeeRepository = employeeRepository;
        this.leaveBalanceMapper = leaveBalanceMapper;
        this.leaveRequestRepository = leaveRequestRepository;
    }

    @Override
    public LeaveBalance initializeLeaveBalance(String employeeId) {
        Optional<Employees> employeeOpt = employeeRepository.findById(employeeId);
        if (employeeOpt.isEmpty()) {
            throw new RuntimeException("Employee not found");
        }

        Employees employees = employeeOpt.get();
        LeaveBalance leaveBalance = new LeaveBalance(
                uuidService.generateUuid(),
                24,
                0,
                0,
                employees
        );
        return leaveBalanceRepository.save(leaveBalance);
    }

    @Override
    public Optional<LeaveBalance> getLeaveBalanceByEmployeeId(String employeeId) {
        Optional<LeaveBalance> balanceOpt = leaveBalanceRepository.findByEmployeeId(employeeId);
        if (balanceOpt.isEmpty()) {
            return Optional.empty();
        }

        LeaveBalance balance = balanceOpt.get();

        // Dynamically calculate usedDays and pendingDays
        int usedDays = leaveRequestRepository.findByEmployeeIdAndStatus(employeeId, LeaveStatus.APPROVED)
                .stream()
                .mapToInt(req -> (int) ChronoUnit.DAYS.between(req.getStartDate(), req.getEndDate()) + 1)
                .sum();

        int pendingDays = leaveRequestRepository.findByEmployeeIdAndStatus(employeeId, LeaveStatus.PENDING)
                .stream()
                .mapToInt(req -> (int) ChronoUnit.DAYS.between(req.getStartDate(), req.getEndDate()) + 1)
                .sum();

        balance.setUsedDays(usedDays);
        balance.setPendingDays(pendingDays);

        return Optional.of(balance);
    }

    @Override
    public String updateLeaveBalance(LeaveBalance leaveBalance) {
        // Keep stored values for totalDays only, but dynamically calculated used/pending will always be updated on fetch
        leaveBalanceRepository.save(leaveBalance);
        return "Updated Leave Balance successfully";
    }

    @Override
    public List<LeaveBalanceResponseDto> getAllLeaveBalances() {
        List<LeaveBalance> leaveBalancesList = leaveBalanceRepository.findAll();
        if (leaveBalancesList.isEmpty()) {
            return new ArrayList<>();
        }

        List<LeaveBalanceResponseDto> result = new ArrayList<>();

        for (LeaveBalance balance : leaveBalancesList) {
            String employeeId = balance.getEmployee().getId();

            // Dynamically calculate usedDays and pendingDays
            int usedDays = leaveRequestRepository.findByEmployeeIdAndStatus(employeeId, LeaveStatus.APPROVED)
                    .stream()
                    .mapToInt(req -> (int) ChronoUnit.DAYS.between(req.getStartDate(), req.getEndDate()) + 1)
                    .sum();

            int pendingDays = leaveRequestRepository.findByEmployeeIdAndStatus(employeeId, LeaveStatus.PENDING)
                    .stream()
                    .mapToInt(req -> (int) ChronoUnit.DAYS.between(req.getStartDate(), req.getEndDate()) + 1)
                    .sum();

            balance.setUsedDays(usedDays);
            balance.setPendingDays(pendingDays);

            result.add(leaveBalanceMapper.toLeaveBalanceResponseDto(balance, balance.getEmployee()));
        }

        return result;
    }
}
