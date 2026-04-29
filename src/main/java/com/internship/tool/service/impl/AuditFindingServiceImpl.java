package com.internship.tool.service.impl;

import com.internship.tool.entity.AuditFinding;
import com.internship.tool.repository.AuditFindingRepository;
import com.internship.tool.service.AuditFindingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public Page<AuditFinding> getAllFindings(int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, buildSort(sortBy, sortDir));
        return repository.findAll(pageable);
    }

    @Override
    public String exportFindingsAsCsv(String sortBy, String sortDir) {
        List<AuditFinding> findings = repository.findAll(buildSort(sortBy, sortDir));

        String header = "id,title,description,severity,status,dueDate";
        String rows = findings.stream()
                .map(this::toCsvRow)
                .collect(Collectors.joining("\n"));

        if (rows.isEmpty()) {
            return header + "\n";
        }

        return header + "\n" + rows + "\n";
    }

    private String toCsvRow(AuditFinding finding) {
        return String.join(",",
                asCsvValue(finding.getId()),
                asCsvValue(finding.getTitle()),
                asCsvValue(finding.getDescription()),
                asCsvValue(finding.getSeverity()),
                asCsvValue(finding.getStatus()),
                asCsvValue(finding.getDueDate()));
    }

    private String asCsvValue(Object value) {
        if (value == null) {
            return "\"\"";
        }

        String text = String.valueOf(value).replace("\"", "\"\"");
        return "\"" + text + "\"";
    }

    private Sort buildSort(String sortBy, String sortDir) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        String safeSortBy = (sortBy == null || sortBy.isBlank()) ? "id" : sortBy;
        return Sort.by(direction, safeSortBy);
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
        existing.setDueDate(updatedFinding.getDueDate());

        return repository.save(existing);
    }

    @Override
    public void deleteFinding(Long id) {
        repository.deleteById(id);
    }
}
