package br.com.fiap.taykarus.motors.application.port.in.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record EditVehicleCommand(
        UUID id,
        String brand,
        String model,
        Integer year,
        String color,
        BigDecimal price
) {
}