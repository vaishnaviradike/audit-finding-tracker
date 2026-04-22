CREATE TABLE audit_finding (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    status VARCHAR(100),
    severity VARCHAR(100),
    created_at TIMESTAMP
);