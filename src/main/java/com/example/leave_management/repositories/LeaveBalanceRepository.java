package com.example.leave_management.repositories;

import com.example.leave_management.models.Employees;
import com.example.leave_management.models.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, String> {
    Optional<LeaveBalance> findByEmployeeId(String employeeId);
}
