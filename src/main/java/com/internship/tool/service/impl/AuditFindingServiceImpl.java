package com.internship.tool.service.impl;

import com.internship.tool.entity.AuditFinding;
import com.internship.tool.repository.AuditFindingRepository;
import com.internship.tool.service.AuditFindingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditFindingServiceImpl implements AuditFindingService {

    @Autowired
    private AuditFindingRepository repository;

    @Override
    public AuditFinding createFinding(AuditFinding finding) {
        finding.setStatus("OPEN"); // default status
        return repository.save(finding);
    }

    @Override
    public List<AuditFinding> getAllFindings() {
        return repository.findAll();
    }

    @Override
    public AuditFinding getFindingById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Finding not found"));
    }

    @Override
    public AuditFinding updateFinding(Long id, AuditFinding updatedFinding) {
        AuditFinding existing = getFindingById(id);

        existing.setTitle(updatedFinding.getTitle());
        existing.setDescription(updatedFinding.getDescription());
        existing.setSeverity(updatedFinding.getSeverity());
        existing.setStatus(updatedFinding.getStatus());

        return repository.save(existing);
    }

    @Override
    public void deleteFinding(Long id) {
        repository.deleteById(id);
    }
}