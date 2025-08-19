package com.example.leave_management.repositories;

import com.example.leave_management.models.LeaveRequest;
import com.example.leave_management.models.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, String> {
    List<LeaveRequest> findByEmployeeIdAndStatus(String employeeId, LeaveStatus status);
    Optional<LeaveRequest> findByIdempotencyKey(String idempotencyKey);
}
