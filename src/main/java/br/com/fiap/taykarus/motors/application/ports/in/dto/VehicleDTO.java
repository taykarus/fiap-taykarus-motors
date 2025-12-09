package br.com.fiap.taykarus.motors.application.ports.in.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record VehicleDTO(
        UUID id,
        String brand,
        String model,
        int year,
        String color,
        BigDecimal price,
        String status,
        LocalDateTime createdDate
) {
}