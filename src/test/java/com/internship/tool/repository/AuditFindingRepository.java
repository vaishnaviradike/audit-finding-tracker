package com.internship.tool.repository;

import com.internship.tool.entity.AuditFinding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditFindingRepository extends JpaRepository<AuditFinding, Long> {

    // Find by status (e.g., OPEN, CLOSED, IN_PROGRESS)
    List<AuditFinding> findByStatus(String status);

    // Search by keyword in title (case-insensitive)
    List<AuditFinding> findByTitleContainingIgnoreCase(String keyword);

    // Filter records by created date range
    List<AuditFinding> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}