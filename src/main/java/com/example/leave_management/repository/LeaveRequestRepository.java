package com.example.leave_management.repository;

import com.example.leave_management.model.LeaveRequest;
import com.example.leave_management.model.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    @Query("""
           SELECT lr FROM LeaveRequest lr
           WHERE lr.employee.id = :employeeId
             AND lr.status IN (:statuses)
             AND NOT (lr.endDate < :startDate OR lr.startDate > :endDate)
           """)
    List<LeaveRequest> findOverlapping(Long employeeId, LocalDate startDate, LocalDate endDate, List<LeaveStatus> statuses);

    List<LeaveRequest> findByEmployeeId(Long employeeId);
}
