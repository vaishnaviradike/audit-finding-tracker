package com.internship.tool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.tool.entity.AuditFinding;
import com.internship.tool.repository.AuditFindingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiIntegrationMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuditFindingRepository auditFindingRepository;

    @BeforeEach
    void cleanUp() {
        auditFindingRepository.deleteAll();
    }

    @Test
    void createFindingReturns200AndBodyStructure() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("title", "SQL Injection");
        request.put("description", "Input validation missing");
        request.put("severity", "HIGH");
        request.put("status", "OPEN");
        request.put("dueDate", "2026-05-01");

        mockMvc.perform(post("/findings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("SQL Injection"))
                .andExpect(jsonPath("$.description").value("Input validation missing"))
                .andExpect(jsonPath("$.severity").value("HIGH"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.dueDate").value("2026-05-01"));
    }

    @Test
    void getAllFindingsReturns200AndPagedBodyStructure() throws Exception {
        auditFindingRepository.save(buildFinding("Issue A", "OPEN", LocalDate.now().plusDays(5)));
        auditFindingRepository.save(buildFinding("Issue B", "CLOSED", LocalDate.now().plusDays(7)));

        mockMvc.perform(get("/findings")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sortBy", "id")
                        .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").isNumber())
                .andExpect(jsonPath("$.content[0].title").exists())
                .andExpect(jsonPath("$.content[0].status").exists())
                .andExpect(jsonPath("$.totalElements").isNumber())
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    void exportFindingsCsvReturns200AndCsvStructure() throws Exception {
        auditFindingRepository.save(buildFinding("CSV Issue", "OPEN", LocalDate.now().plusDays(2)));

        mockMvc.perform(get("/findings/export")
                        .param("sortBy", "id")
                        .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", containsString("audit-findings.csv")))
                .andExpect(content().contentType("text/csv"))
                .andExpect(content().string(containsString("id,title,description,severity,status,dueDate")))
                .andExpect(content().string(containsString("CSV Issue")));
    }

    @Test
    void getFindingByIdReturns200AndBodyStructure() throws Exception {
        AuditFinding saved = auditFindingRepository.save(buildFinding("ID Lookup", "OPEN", LocalDate.now().plusDays(4)));

        mockMvc.perform(get("/findings/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.title").value("ID Lookup"))
                .andExpect(jsonPath("$.severity").exists())
                .andExpect(jsonPath("$.status").exists());
    }

    @Test
    void updateFindingReturns200AndUpdatedBodyStructure() throws Exception {
        AuditFinding saved = auditFindingRepository.save(buildFinding("Before Update", "OPEN", LocalDate.now().plusDays(3)));

        Map<String, Object> request = new HashMap<>();
        request.put("title", "After Update");
        request.put("description", "Updated description");
        request.put("severity", "MEDIUM");
        request.put("status", "IN_PROGRESS");
        request.put("dueDate", "2026-05-10");

        mockMvc.perform(put("/findings/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.title").value("After Update"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.severity").value("MEDIUM"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.dueDate").value("2026-05-10"));
    }

    @Test
    void deleteFindingReturns200AndMessageBody() throws Exception {
        AuditFinding saved = auditFindingRepository.save(buildFinding("Delete Me", "OPEN", LocalDate.now().plusDays(1)));

        mockMvc.perform(delete("/findings/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted successfully"));
    }

    @Test
    void updateAuditReturns200AndStringBody() throws Exception {
        Map<String, Object> request = Map.of("title", "Temp");

        mockMvc.perform(put("/api/audit/{id}", 100)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Update API working for id: 100")));
    }

    @Test
    void deleteAuditReturns204() throws Exception {
        mockMvc.perform(delete("/api/audit/{id}", 200))
                .andExpect(status().isNoContent());
    }

    @Test
    void searchAuditReturns200AndPagedBodyStructure() throws Exception {
        mockMvc.perform(get("/api/audit/search")
                        .param("q", "risk")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0]").isString())
                .andExpect(jsonPath("$.totalElements").isNumber())
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    void getAuditStatsReturns200AndStatsStructure() throws Exception {
        mockMvc.perform(get("/api/audit/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").isNumber())
                .andExpect(jsonPath("$.open").isNumber())
                .andExpect(jsonPath("$.closed").isNumber());
    }

    @Test
    void authLoginSuccessReturns200AndTokenStructure() throws Exception {
        Map<String, String> request = Map.of(
                "username", "admin",
                "password", "password"
        );

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void authLoginFailureReturns401AndErrorMessage() throws Exception {
        Map<String, String> request = Map.of(
                "username", "bad",
                "password", "bad"
        );

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    void authRegisterReturns200AndBodyStructure() throws Exception {
        Map<String, String> request = Map.of(
                "username", "newuser",
                "password", "secret"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.role").value("VIEWER"));
    }

    @Test
    void authRefreshReturns200AndTokenStructure() throws Exception {
        mockMvc.perform(post("/auth/refresh"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString());
    }

    private AuditFinding buildFinding(String title, String status, LocalDate dueDate) {
        AuditFinding finding = new AuditFinding();
        finding.setTitle(title);
        finding.setDescription("Description for " + title);
        finding.setSeverity("HIGH");
        finding.setStatus(status);
        finding.setDueDate(dueDate);
        return finding;
    }
}
