# Tool-23 Security Analysis

**Author:** AI Developer 3  
**Status:** Initial Draft (Day 1)  

---

## 1. Project Security Overview
As we initiate the development of the Tool-23 Audit Finding Tracker, we must consider the security implications of the web application built in the capstone project. 

Following are the list of topics:
* 5 OWASP top 10 security risks with attack scenarios and mitigation for each

---

## 2. OWASP top 10 security risks with attack scenarios and mitigation for each

### A01:2021 – Broken Access Control
* **Conceptual Scenario:** In a multi-role application (ADMIN/MANAGER/VIEWER), a user might discover that while certain buttons are hidden on the UI, the underlying REST endpoints remain accessible via manual requests.
* **Potential Mitigation:** We propose investigating server-side role validation, role validation for the given roles to ensure access rights are verified at the API level as well as entry points to various parts of the application.

### A03:2021 – Injection
* **Conceptual Scenario:** Applications accepting text input for audit descriptions or searches could be vulnerable if that input is treated as executable code, potentially interfering with database logic.
* **Potential Mitigation:** We may take security measures against common injection tactics like sql injections, input sanitization, prompt injections, etc at entry points to api as well as exit points. We may make use of Spring Data JPA, which utilizes parameter binding to treat user input as data rather than executable code. 

### A07:2021 – Identification and Authentication Failures
* **Conceptual Scenario:** If session management lacks a strict expiration policy, a compromised JWT token could grant unauthorized access for an indefinite period.
* **Potential Mitigation:** We aim to implement JWT validation with appropriate expiration times and may utilize Redis for session or response caching to manage data exposure.

### A05:2021 – Security Misconfiguration
* **Conceptual Scenario:** In containerized environments, leaving default credentials or exposing diagnostic tools like Swagger UI to the public may provide an attack roadmap.
* **Potential Mitigation:** Our strategy is to centralize sensitive configurations in environment variables via `.env` files and ensure development tools are secured in the final deployment.

### A10:2021 – Server-Side Request Forgery (SSRF)
* **Conceptual Scenario:** If a service fetches external resources based on user-provided links, an attacker could potentially trick the server into requesting sensitive data from internal ports.
* **Potential Mitigation:** We may implement a strict whitelist for external requests and ensure internal service communication is isolated within the Docker network.

---