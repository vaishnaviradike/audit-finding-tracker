package com.internship.tool.controller;

import com.internship.tool.entity.AuditFinding;
import com.internship.tool.service.AuditFindingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/findings")
public class AuditFindingController {

    @Autowired
    private AuditFindingService service;

    // Create
    @PostMapping
    public AuditFinding create(@RequestBody AuditFinding finding) {
        return service.createFinding(finding);
    }

    // Get All with pagination + sorting
    @GetMapping
    public Page<AuditFinding> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return service.getAllFindings(page, size, sortBy, sortDir);
    }

    // Export CSV
    @GetMapping(value = "/export", produces = "text/csv")
    public ResponseEntity<byte[]> exportCsv(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        String csvContent = service.exportFindingsAsCsv(sortBy, sortDir);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=audit-findings.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvContent.getBytes(StandardCharsets.UTF_8));
    }

    // Get by ID
    @GetMapping("/{id}")
    public AuditFinding getById(@PathVariable Long id) {
        return service.getFindingById(id);
    }

    // Update
    @PutMapping("/{id}")
    public AuditFinding update(@PathVariable Long id, @RequestBody AuditFinding finding) {
        return service.updateFinding(id, finding);
    }

    // Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteFinding(id);
        return "Deleted successfully";
    }
}
