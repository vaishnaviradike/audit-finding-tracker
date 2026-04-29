package com.internship.tool.repository;

import com.internship.tool.entity.AuditFinding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AuditFindingRepository extends JpaRepository<AuditFinding, Long> {
    List<AuditFinding> findByDueDateBeforeAndStatusIgnoreCaseNot(LocalDate dueDate, String status);

    List<AuditFinding> findByDueDateBetweenAndStatusIgnoreCaseNot(LocalDate startDate, LocalDate endDate, String status);

    long countByStatusIgnoreCase(String status);

    long countByDueDateBeforeAndStatusIgnoreCaseNot(LocalDate dueDate, String status);
}
