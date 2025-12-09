package br.com.fiap.taykarus.motors.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID entityId;
    private String entityType;
    private LocalDateTime timestamp;
    private String action;

    public AuditLogEntity(UUID entityId, String entityType, LocalDateTime timestamp, String action) {
        this.entityId = entityId;
        this.entityType = entityType;
        this.timestamp = timestamp;
        this.action = action;
    }
}