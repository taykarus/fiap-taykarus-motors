package br.com.fiap.taykarus.motors.domain.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PriceTest {
    @Test
    @DisplayName("Price should accept positive values")
    void validPrice() {
        Price p = new Price(new BigDecimal("10.00"));
        assertEquals(new BigDecimal("10.00"), p.value());
    }

    @Test
    @DisplayName("Price should reject null, zero or negative values")
    void invalidPrice() {
        assertThrows(IllegalArgumentException.class, () -> new Price(null));
        assertThrows(IllegalArgumentException.class, () -> new Price(BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> new Price(new BigDecimal("-1.00")));
    }
}