package com.example.leave_management.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "leave_balances")
public class LeaveBalance implements Serializable {
    @Id
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "total_days")
    private Integer totalDays;

    @Column(name = "used_days")
    private Integer usedDays;

    @Column(name = "pending_days")
    private Integer pendingDays;

    @OneToOne
    @JoinColumn(name = "employee_id",nullable = false)
    @JsonBackReference
    private Employees employee;
}
