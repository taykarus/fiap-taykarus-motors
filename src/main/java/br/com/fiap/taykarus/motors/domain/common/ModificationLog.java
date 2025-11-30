package br.com.fiap.taykarus.motors.domain.common;

import java.time.LocalDateTime;

public record ModificationLog(java.util.UUID entityId, String entityType, LocalDateTime timestamp, String action) {
    public ModificationLog {
        if (entityId == null || entityType == null || timestamp == null || action == null) {
            throw new IllegalArgumentException("Log requires Entity ID, Type, Timestamp and Action");
        }
    }
}