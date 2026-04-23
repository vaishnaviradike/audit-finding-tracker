package com.internship.tool.repository;

import com.internship.tool.entity.AuditFinding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditFindingRepository extends JpaRepository<AuditFinding, Long> {
}