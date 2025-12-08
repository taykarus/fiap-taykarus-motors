package br.com.fiap.taykarus.motors.application.ports.in.dto;

import java.math.BigDecimal;

public record RegisterVehicleCommand(
        String brand,
        String model,
        int year,
        String color,
        BigDecimal price
) {
}
