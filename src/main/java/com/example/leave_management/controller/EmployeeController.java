package com.example.leave_management.controller;

import com.example.leave_management.dto.EmployeeRequest;
import com.example.leave_management.model.Employee;
import com.example.leave_management.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "https://stirring-tulumba-b45557.netlify.app")  // allow frontend React
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employees;

    public EmployeeController(EmployeeService employees) {
        this.employees = employees;
    }

    // ✅ Add new employee (POST)
    @PostMapping
    public ResponseEntity<Employee> add(@Valid @RequestBody EmployeeRequest req) {
        return ResponseEntity.ok(employees.addEmployee(req));
    }

    // ✅ Get employee by id (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Employee> get(@PathVariable Long id) {
        return ResponseEntity.ok(employees.get(id));
    }

    // ✅ Get leave balance for employee (GET)
    @GetMapping("/{id}/leave-balance")
    public ResponseEntity<Integer> balance(@PathVariable Long id) {
        return ResponseEntity.ok(employees.getLeaveBalance(id));
    }

    // ✅ Get all employees (GET)
    @GetMapping
    public ResponseEntity<List<Employee>> getAll() {
        return ResponseEntity.ok(employees.getAllEmployees());
    }
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchByName(@RequestParam String name) {
        List<Employee> results = employees.findByNameContainingIgnoreCase(name);
        return ResponseEntity.ok(results);
    }

}
