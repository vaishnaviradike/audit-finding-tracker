package com.internship.tool.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuditService {

    public Object updateAudit(Long id, Object request) {
        return "Update API working for id: " + id;
    }

    public void softDeleteAudit(Long id) {
        System.out.println("Soft delete called for id: " + id);
    }

    public Page<Object> searchAudit(String q, int page, int size, String sortBy, String sortDir) {
        List<Object> allResults = List.of("Search result for: " + q);
        int start = Math.min(page * size, allResults.size());
        int end = Math.min(start + size, allResults.size());

        List<Object> pageContent = allResults.subList(start, end);
        return new PageImpl<>(pageContent, PageRequest.of(page, size), allResults.size());
    }

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", 10);
        stats.put("open", 5);
        stats.put("closed", 5);
        return stats;
    }
}
