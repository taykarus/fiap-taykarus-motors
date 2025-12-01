package br.com.fiap.taykarus.motors.domain.sale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CpfTest {
    @Test
    @DisplayName("CPF should clean formatting characters and accept valid length")
    void validCpf() {
        // Test with dots and dash
        Cpf cpf = new Cpf("123.456.789-00");
        assertEquals("12345678900", cpf.value());

        // Test raw numbers
        Cpf cpfRaw = new Cpf("12345678900");
        assertEquals("12345678900", cpfRaw.value());
    }

    @Test
    @DisplayName("CPF should reject null, invalid length or characters")
    void invalidCpf() {
        assertThrows(IllegalArgumentException.class, () -> new Cpf(null));
        assertThrows(IllegalArgumentException.class, () -> new Cpf("123")); // Too short
        assertThrows(IllegalArgumentException.class, () -> new Cpf("ABC.456.789-00")); // Letters
    }
}