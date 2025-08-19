package com.example.leave_management.service;

import com.example.leave_management.dto.EmployeeRequest;
import com.example.leave_management.model.Employee;
import com.example.leave_management.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employees;

    public EmployeeService(EmployeeRepository employees) {
        this.employees = employees;
    }

    public Employee addEmployee(EmployeeRequest req) {
        employees.findByEmail(req.getEmail()).ifPresent(e -> {
            throw new IllegalArgumentException("Email already exists: " + req.getEmail());
        });

        int balance = (req.getInitialLeaveBalance() == null) ? 20 : req.getInitialLeaveBalance();
        Employee emp = new Employee(
                req.getName(),
                req.getEmail(),
                req.getDepartment(),
                req.getJoiningDate(),
                balance
        );
        return employees.save(emp);
    }

    public Employee get(Long id) {
        return employees.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Employee not found: " + id));
    }

    public int getLeaveBalance(Long id) {
        return get(id).getLeaveBalance();
    }

    public List<Employee> getAllEmployees() {
        return employees.findAll();   // ✅ Correct usage
    }
    public List<Employee> findByNameContainingIgnoreCase(String name) {
        return employees.findByNameContainingIgnoreCase(name);
    }

}
