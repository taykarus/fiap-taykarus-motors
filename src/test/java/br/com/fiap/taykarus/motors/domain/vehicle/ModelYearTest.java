package br.com.fiap.taykarus.motors.domain.vehicle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ModelYearTest {
    @Test
    @DisplayName("ModelYear should accept valid years")
    void validYear() {
        int currentYear = LocalDate.now().getYear();
        ModelYear year = new ModelYear(currentYear);
        assertEquals(currentYear, year.value());
    }

    @Test
    @DisplayName("ModelYear should reject improbable years")
    void invalidYear() {
        assertThrows(IllegalArgumentException.class, () -> new ModelYear(1800)); // Too old

        int farFuture = LocalDate.now().getYear() + 5;
        assertThrows(IllegalArgumentException.class, () -> new ModelYear(farFuture)); // Too far future
    }
}