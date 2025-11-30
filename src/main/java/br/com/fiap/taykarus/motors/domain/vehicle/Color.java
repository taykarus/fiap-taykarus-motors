package br.com.fiap.taykarus.motors.domain.vehicle;

public record Color(String value) {
    public Color {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Color cannot be empty");
        }
    }
}