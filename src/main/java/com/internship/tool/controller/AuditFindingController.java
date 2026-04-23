package com.internship.tool.controller;

import com.internship.tool.entity.AuditFinding;
import com.internship.tool.service.AuditFindingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // Get All
    @GetMapping
    public List<AuditFinding> getAll() {
        return service.getAllFindings();
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