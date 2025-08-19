package com.example.leave_management.services.employees_service;

import com.example.leave_management.mappers.EmployeeMapper;
import com.example.leave_management.models.Employees;
import com.example.leave_management.models.LeaveBalance;
import com.example.leave_management.repositories.EmployeeRepository;
import com.example.leave_management.repositories.LeaveBalanceRepository;
import com.example.leave_management.utils.UUIDService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    private final LeaveBalanceRepository leaveBalanceRepository;
    private EmployeeMapper employeeMapper;
    private EmployeeRepository employeeRepository;
    private UUIDService uuidService;

    EmployeeServiceImpl(EmployeeMapper employeeMapper, EmployeeRepository employeeRepository, UUIDService uuidService, LeaveBalanceRepository leaveBalanceRepository) {
        this.employeeMapper = employeeMapper;
        this.employeeRepository = employeeRepository;
        this.uuidService = uuidService;
        this.leaveBalanceRepository = leaveBalanceRepository;
    }

    @Override
    public String createEmployee(String name, String email, String department, LocalDate joiningDate) {
        Employees employees = employeeMapper.ToEmployee(uuidService.generateUuid(), name, email, department, joiningDate);
        Employees savedEmployees = employeeRepository.save(employees);
        LeaveBalance leaveBalance = new LeaveBalance(
                uuidService.generateUuid(),
                24,
                0,
                0,
                savedEmployees
        );
        leaveBalanceRepository.save(leaveBalance);
        return "Employee created successfully";
    }

    @Override
    public String deleteEmployeeById(String id) {
        Optional<Employees> employee = employeeRepository.findById(id);
        if(employee.isEmpty()){
            return "Employee not found";
        }
        employeeRepository.deleteById(id);
        return "Employee deleted successfully";
    }

    @Override
    public List<Employees> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employees> getEmployeeById(String employeeId) {
        Optional<Employees> employee = employeeRepository.findById(employeeId);
        if(employee.isEmpty()){
            throw new IllegalArgumentException("Employee not found");
        }
        return employee;
    }
}
