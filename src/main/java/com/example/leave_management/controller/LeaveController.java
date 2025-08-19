package com.example.leave_management.controller;

import com.example.leave_management.dto.LeaveApplyRequest;
import com.example.leave_management.dto.LeaveResponse;
import com.example.leave_management.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@CrossOrigin(origins = "http://localhost:3000")
public class LeaveController {

    private final LeaveService leaves;

    public LeaveController(LeaveService leaves) {
        this.leaves = leaves;
    }

    @PostMapping("/apply")
    public ResponseEntity<LeaveResponse> apply(@Valid @RequestBody LeaveApplyRequest req) {
        return ResponseEntity.ok(leaves.apply(req));
    }

    @PutMapping("/{leaveId}/approve")
    public ResponseEntity<LeaveResponse> approve(@PathVariable Long leaveId) {
        return ResponseEntity.ok(leaves.approve(leaveId));
    }

    @PutMapping("/{leaveId}/reject")
    public ResponseEntity<LeaveResponse> reject(@PathVariable Long leaveId) {
        return ResponseEntity.ok(leaves.reject(leaveId));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveResponse>> list(@PathVariable Long employeeId) {
        return ResponseEntity.ok(leaves.listByEmployee(employeeId));
    }

    @GetMapping("/employee/{employeeId}/balance")
    public ResponseEntity<Integer> balance(@PathVariable Long employeeId) {
        return ResponseEntity.ok(leaves.getBalance(employeeId));
    }

    // ✅ Endpoint for Manager Dashboard
    @GetMapping("/all")
    public ResponseEntity<List<LeaveResponse>> getAllLeaves() {
        return ResponseEntity.ok(leaves.listAll());
    }
}
