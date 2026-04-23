package com.internship.tool.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuditService {

    public Object updateAudit(Long id, Object request) {
        return "Update API working for id: " + id;
    }

    public void softDeleteAudit(Long id) {
        System.out.println("Soft delete called for id: " + id);
    }

    public List<Object> searchAudit(String q) {
        return List.of("Search result for: " + q);
    }

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", 10);
        stats.put("open", 5);
        stats.put("closed", 5);
        return stats;
    }
}