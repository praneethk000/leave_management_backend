package com.example.leave_management.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employees")
public class Employees implements Serializable {
    @Id
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "email",nullable = false)
    private String email;

    @Column(name = "department",nullable = false)
    private String department;

    @Column(name = "joining_date",nullable = false)
    private LocalDate joiningDate;

    @Column(name = "created_at",nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private LeaveBalance leaveBalance;
}
