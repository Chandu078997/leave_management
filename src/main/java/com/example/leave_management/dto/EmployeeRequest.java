package com.example.leave_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class EmployeeRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Department is required")
    private String department;

    @NotNull(message = "Joining date is required")
    private LocalDate joiningDate;

    @Min(0) @Max(365)
    private Integer initialLeaveBalance; // optional; default 20 if null

    public EmployeeRequest() {}

    public EmployeeRequest(String name, String email, String department, LocalDate joiningDate, Integer initialLeaveBalance) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.joiningDate = joiningDate;
        this.initialLeaveBalance = initialLeaveBalance;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }

    public Integer getInitialLeaveBalance() { return initialLeaveBalance; }
    public void setInitialLeaveBalance(Integer initialLeaveBalance) { this.initialLeaveBalance = initialLeaveBalance; }
}
