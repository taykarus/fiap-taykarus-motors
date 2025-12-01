package br.com.fiap.taykarus.motors.domain.vehicle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ColorTest {
    @Test
    @DisplayName("Color should return correct value")
    void validColorValue() {
        Color color = new Color("Blue");
        assertEquals("Blue", color.value());
    }

    @Test
    @DisplayName("Color should reject empty values")
    void invalidColor() {
        assertThrows(IllegalArgumentException.class, () -> new Color(null));
        assertThrows(IllegalArgumentException.class, () -> new Color(""));
        assertThrows(IllegalArgumentException.class, () -> new Color("   "));
    }
}