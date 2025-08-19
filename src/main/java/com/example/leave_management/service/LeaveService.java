package com.example.leave_management.service;

import com.example.leave_management.dto.LeaveApplyRequest;
import com.example.leave_management.dto.LeaveResponse;
import com.example.leave_management.model.Employee;
import com.example.leave_management.model.LeaveRequest;
import com.example.leave_management.model.LeaveStatus;
import com.example.leave_management.repository.EmployeeRepository;
import com.example.leave_management.repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.time.LocalDate;

@Service
@Transactional
public class LeaveService {

    private final LeaveRequestRepository leaves;
    private final EmployeeRepository employees;

    public LeaveService(LeaveRequestRepository leaves, EmployeeRepository employees) {
        this.leaves = leaves;
        this.employees = employees;
    }

    public LeaveResponse apply(LeaveApplyRequest req) {
        Employee emp = employees.findById(req.getEmployeeId())
                .orElseThrow(() -> new NoSuchElementException("Employee not found: " + req.getEmployeeId()));

        LocalDate start = req.getStartDate();
        LocalDate end = req.getEndDate();

        if (end.isBefore(start))
            throw new IllegalArgumentException("End date cannot be before start date");

        if (start.isBefore(emp.getJoiningDate()))
            throw new IllegalArgumentException("Cannot apply leave before joining date");

        long days = ChronoUnit.DAYS.between(start, end) + 1;
        if (days <= 0) throw new IllegalArgumentException("Invalid leave duration");
        if (days > emp.getLeaveBalance())
            throw new IllegalArgumentException("Insufficient leave balance");

        var overlapping = leaves.findOverlapping(
                emp.getId(),
                start,
                end,
                List.copyOf(EnumSet.of(LeaveStatus.APPROVED, LeaveStatus.PENDING))
        );
        if (!overlapping.isEmpty())
            throw new IllegalArgumentException("Overlapping leave exists for the requested dates");

        LeaveRequest lr = new LeaveRequest(start, end, LeaveStatus.PENDING, emp, req.getReason());
        lr = leaves.save(lr);
        return toDtoWithAppliedAt(lr);
    }

    public LeaveResponse approve(Long leaveId) {
        LeaveRequest lr = leaves.findById(leaveId)
                .orElseThrow(() -> new NoSuchElementException("Leave request not found: " + leaveId));

        if (lr.getStatus() != LeaveStatus.PENDING)
            throw new IllegalStateException("Only PENDING leaves can be approved");

        Employee emp = lr.getEmployee();
        long days = ChronoUnit.DAYS.between(lr.getStartDate(), lr.getEndDate()) + 1;

        if (days > emp.getLeaveBalance())
            throw new IllegalStateException("Not enough balance to approve");

        emp.setLeaveBalance(emp.getLeaveBalance() - (int) days);
        lr.setStatus(LeaveStatus.APPROVED);

        employees.save(emp);
        leaves.save(lr);

        return toDtoWithAppliedAt(lr);
    }

    public LeaveResponse reject(Long leaveId) {
        LeaveRequest lr = leaves.findById(leaveId)
                .orElseThrow(() -> new NoSuchElementException("Leave request not found: " + leaveId));

        if (lr.getStatus() != LeaveStatus.PENDING)
            throw new IllegalStateException("Only PENDING leaves can be rejected");

        lr.setStatus(LeaveStatus.REJECTED);
        lr = leaves.save(lr);
        return toDtoWithAppliedAt(lr);
    }

    public int getBalance(Long employeeId) {
        return employees.findById(employeeId)
                .map(Employee::getLeaveBalance)
                .orElseThrow(() -> new NoSuchElementException("Employee not found: " + employeeId));
    }

    public List<LeaveResponse> listByEmployee(Long employeeId) {
        employees.findById(employeeId).orElseThrow(() -> new NoSuchElementException("Employee not found: " + employeeId));
        return leaves.findByEmployeeId(employeeId).stream().map(this::toDtoWithAppliedAt).toList();
    }

    public List<LeaveResponse> listAll() {
        return leaves.findAll()
                     .stream()
                     .map(this::toDtoWithAppliedAt)
                     .toList();
    }

    private LeaveResponse toDtoWithAppliedAt(LeaveRequest lr) {
        LeaveResponse dto = new LeaveResponse(
                lr.getId(),
                lr.getEmployee().getId(),
                lr.getEmployee().getName(),
                lr.getStartDate(),
                lr.getEndDate(),
                lr.getStatus(),
                lr.getReason()
        );
        dto.setAppliedAt(lr.getAppliedAt());
        return dto;
    }
}
