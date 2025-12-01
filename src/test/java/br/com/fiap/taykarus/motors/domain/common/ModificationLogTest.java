package br.com.fiap.taykarus.motors.domain.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ModificationLogTest {

    @Test
    @DisplayName("Should create a valid ModificationLog")
    void shouldCreateValidLog() {
        UUID entityId = UUID.randomUUID();
        String entityType = "VEHICLE";
        LocalDateTime now = LocalDateTime.now();
        String action = "CREATED";

        ModificationLog log = new ModificationLog(entityId, entityType, now, action);

        assertNotNull(log);
        assertEquals(entityId, log.entityId());
        assertEquals(entityType, log.entityType());
        assertEquals(now, log.timestamp());
        assertEquals(action, log.action());
    }

    @Test
    @DisplayName("Should reject null values in constructor")
    void shouldRejectNulls() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        // 1. Null Entity ID
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () ->
                new ModificationLog(null, "VEHICLE", now, "ACTION")
        );
        assertEquals("Log requires Entity ID, Type, Timestamp and Action", ex1.getMessage());

        // 2. Null Entity Type
        assertThrows(IllegalArgumentException.class, () ->
                new ModificationLog(id, null, now, "ACTION")
        );

        // 3. Null Timestamp
        assertThrows(IllegalArgumentException.class, () ->
                new ModificationLog(id, "VEHICLE", null, "ACTION")
        );

        // 4. Null Action
        assertThrows(IllegalArgumentException.class, () ->
                new ModificationLog(id, "VEHICLE", now, null)
        );
    }
}