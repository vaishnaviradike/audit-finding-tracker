package com.internship.tool.service;

import com.internship.tool.entity.AuditFinding;
import org.springframework.data.domain.Page;

public interface AuditFindingService {
    AuditFinding createFinding(AuditFinding finding);

    Page<AuditFinding> getAllFindings(int page, int size, String sortBy, String sortDir);

    String exportFindingsAsCsv(String sortBy, String sortDir);

    AuditFinding getFindingById(Long id);

    AuditFinding updateFinding(Long id, AuditFinding finding);

    void deleteFinding(Long id);
}
