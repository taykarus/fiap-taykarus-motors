package br.com.fiap.taykarus.motors.application.ports.in.dto;

import java.util.UUID;

public record SellVehicleCommand(
        UUID vehicleId,
        String buyerCpf
) {
}