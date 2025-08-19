package com.example.leave_management.services.leave_request_service;

import com.example.leave_management.dto.response.LeaveRequestResponseDto;
import com.example.leave_management.mappers.LeaveRequestMapper;
import com.example.leave_management.models.Employees;
import com.example.leave_management.models.LeaveBalance;
import com.example.leave_management.models.LeaveRequest;
import com.example.leave_management.models.LeaveStatus;
import com.example.leave_management.repositories.EmployeeRepository;
import com.example.leave_management.repositories.LeaveBalanceRepository;
import com.example.leave_management.repositories.LeaveRequestRepository;
import com.example.leave_management.utils.UUIDService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final UUIDService uuidService;
    private final EmployeeRepository employeeRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveRequestMapper leaveRequestMapper;

    public LeaveRequestServiceImpl(LeaveBalanceRepository leaveBalanceRepository, UUIDService uuidService, EmployeeRepository employeeRepository, LeaveRequestRepository leaveRequestRepository, LeaveRequestMapper leaveRequestMapper) {
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.uuidService = uuidService;
        this.employeeRepository = employeeRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveRequestMapper = leaveRequestMapper;
    }

//    @Transactional
//    @Override
//    public String applyForLeave(String employeeId, LocalDate startDate, LocalDate endDate, String reason, String idempotencyKey) {
//        long requestedDays = ChronoUnit.DAYS.between(startDate, endDate)+1;
//
//        Optional<LeaveRequest> existingRequest = leaveRequestRepository.findByEmployeeIdAndStatus(employeeId, LeaveStatus.PENDING);
//        if (existingRequest.isPresent()) {
//            return "You already have a pending leave request. Please wait until it is processed.";
//        }
//
//        Optional<LeaveBalance> balanceOpt = leaveBalanceRepository.findByEmployeeId(employeeId);
//        if(balanceOpt.isEmpty()){
//            return "Leave not found";
//        }
//
//
//        LeaveBalance balance = balanceOpt.get();
//        int availableDays = balance.getTotalDays() - balance.getUsedDays() - balance.getPendingDays();
//
//        if(requestedDays >availableDays){
//            return "Requested Days exceeded";
//        }
//
//        Optional<Employees> employeesOpt = employeeRepository.findById(employeeId);
//        if(employeesOpt.isEmpty()){
//            return "Employee not found";
//        }
//        Employees employees = employeesOpt.get();
//
//        balance.setPendingDays(balance.getPendingDays() + (int)requestedDays);
//        leaveBalanceRepository.save(balance);
//
//        LeaveRequest request = new LeaveRequest(
//                uuidService.generateUuid(),
//                startDate,
//                endDate,
//                reason,
//                LeaveStatus.PENDING,
//                idempotencyKey,
//                null,
//                employees
//        );
//        leaveRequestRepository.save(request);
//        return "Leave Request submitted";
//    }
@Transactional
@Override
public String applyForLeave(String employeeId, LocalDate startDate, LocalDate endDate, String reason, String idempotencyKey) {

    long requestedDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

    List<LeaveRequest> existingRequest = leaveRequestRepository.findByEmployeeIdAndStatus(employeeId, LeaveStatus.PENDING);
    if (!existingRequest.isEmpty()) {
        return "You already have a pending leave request. Please wait until it is processed.";
    }

    Optional<LeaveBalance> balanceOpt = leaveBalanceRepository.findByEmployeeId(employeeId);
    if(balanceOpt.isEmpty()){
        return "Leave balance not found";
    }

    LeaveBalance balance = balanceOpt.get();

    // Calculate available days dynamically
    int totalUsedDays = leaveRequestRepository.findByEmployeeIdAndStatus(employeeId, LeaveStatus.APPROVED)
            .stream()
            .mapToInt(req -> (int)ChronoUnit.DAYS.between(req.getStartDate(), req.getEndDate()) + 1)
            .sum();

    int totalPendingDays = leaveRequestRepository.findByEmployeeIdAndStatus(employeeId, LeaveStatus.PENDING)
            .stream()
            .mapToInt(req -> (int)ChronoUnit.DAYS.between(req.getStartDate(), req.getEndDate()) + 1)
            .sum();

    int availableDays = balance.getTotalDays() - totalUsedDays - totalPendingDays;

    if(requestedDays > availableDays){
        return "Requested Days exceeded";
    }

    Optional<Employees> employeesOpt = employeeRepository.findById(employeeId);
    if(employeesOpt.isEmpty()){
        return "Employee not found";
    }
    Employees employees = employeesOpt.get();

    // Save the new leave request
    LeaveRequest request = new LeaveRequest(
            uuidService.generateUuid(),
            startDate,
            endDate,
            reason,
            LeaveStatus.PENDING,
            idempotencyKey,
            null,
            employees
    );
    leaveRequestRepository.save(request);

    // Recalculate pending days dynamically
    totalPendingDays += requestedDays;
    balance.setPendingDays(totalPendingDays);
    balance.setUsedDays(totalUsedDays); // used stays same
    leaveBalanceRepository.save(balance);

    return "Leave Request submitted";
}


    @Override
    @Transactional
    public String updateLeaveStatus(String id, String status) {
        Optional<LeaveRequest> leaveRequestOpt = leaveRequestRepository.findById(id);
        if (leaveRequestOpt.isEmpty()) {
            return "Leave not found";
        }
        LeaveRequest leaveRequest = leaveRequestOpt.get();

        if (!leaveRequest.getStatus().equals(LeaveStatus.PENDING)) {
            return "Leave status is already processed";
        }

        Optional<LeaveBalance> balanceOpt = leaveBalanceRepository.findByEmployeeId(leaveRequest.getEmployee().getId());
        if(balanceOpt.isEmpty()){
            return "Leave Balance not found";
        }

        LeaveBalance balance = balanceOpt.get();

        // Update leave status
        if(status.equalsIgnoreCase("APPROVED")){
            leaveRequest.setStatus(LeaveStatus.APPROVED);
        } else if(status.equalsIgnoreCase("REJECTED")){
            leaveRequest.setStatus(LeaveStatus.REJECTED);
        } else {
            return "Invalid Leave Status";
        }
        leaveRequestRepository.save(leaveRequest);

        // Recalculate usedDays and pendingDays dynamically
        int totalUsedDays = leaveRequestRepository.findByEmployeeIdAndStatus(leaveRequest.getEmployee().getId(), LeaveStatus.APPROVED)
                .stream()
                .mapToInt(req -> (int)ChronoUnit.DAYS.between(req.getStartDate(), req.getEndDate()) + 1)
                .sum();

        int totalPendingDays = leaveRequestRepository.findByEmployeeIdAndStatus(leaveRequest.getEmployee().getId(), LeaveStatus.PENDING)
                .stream()
                .mapToInt(req -> (int)ChronoUnit.DAYS.between(req.getStartDate(), req.getEndDate()) + 1)
                .sum();

        balance.setUsedDays(totalUsedDays);
        balance.setPendingDays(totalPendingDays);
        leaveBalanceRepository.save(balance);

        return "Leave request " + leaveRequest.getStatus() + " successfully";
    }


    @Override
    public List<LeaveRequestResponseDto> getAllLeaveRequests() {
        List<LeaveRequest> leaveRequestList = leaveRequestRepository.findAll();
        if(leaveRequestList.isEmpty()){
            return new ArrayList<>();
        }
        return leaveRequestList.stream().map(leaveRequest ->
            leaveRequestMapper.toLeaveRequestResponseDto(leaveRequest,leaveRequest.getEmployee())).toList();

    }
}
