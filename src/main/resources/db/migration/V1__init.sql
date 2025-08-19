-- src/main/resources/db/migration/V1__init.sql

CREATE TABLE employees (
                           id CHAR(36) PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           email VARCHAR(100)  NOT NULL,
                           department VARCHAR(100) NOT NULL,
                           joining_date DATE NOT NULL,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE leave_balances (
                                id CHAR(36) PRIMARY KEY,
                                employee_id CHAR(36) NOT NULL,
                                total_days INTEGER NOT NULL DEFAULT 24,
                                used_days INTEGER NOT NULL DEFAULT 0,
                                pending_days INTEGER NOT NULL DEFAULT 0,
                                FOREIGN KEY (employee_id) REFERENCES employees(id)
);

CREATE TABLE leave_requests (
                                id CHAR(36) PRIMARY KEY,
                                employee_id CHAR(36) NOT NULL,
                                start_date DATE NOT NULL,
                                end_date DATE NOT NULL,
                                reason VARCHAR(255) NOT NULL,
                                status ENUM('PENDING','APPROVED','REJECTED','CANCELLED') NOT NULL,
                                idempotency_key VARCHAR(255) UNIQUE NOT NULL,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (employee_id) REFERENCES employees(id)
);

-- Indexes for faster lookups
# CREATE INDEX idx_leave_requests_employee_id ON leave_requests(employee_id);
# CREATE INDEX idx_leave_requests_idempotency_key ON leave_requests(idempotency_key);
