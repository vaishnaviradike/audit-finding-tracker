package com.internship.tool.service;

import com.internship.tool.entity.AuditFinding;

import java.util.List;

public interface AuditFindingService {
    AuditFinding createFinding(AuditFinding finding);

    List<AuditFinding> getAllFindings();

    AuditFinding getFindingById(Long id);

    AuditFinding updateFinding(Long id, AuditFinding finding);

    void deleteFinding(Long id);
}
