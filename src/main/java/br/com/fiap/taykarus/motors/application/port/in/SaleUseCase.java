package br.com.fiap.taykarus.motors.application.port.in;

import br.com.fiap.taykarus.motors.application.port.in.dto.SellVehicleCommand;

import java.util.UUID;

public interface SaleUseCase {
    UUID sellVehicle(SellVehicleCommand command);

    void confirmSale(UUID saleId);

    void cancelSale(UUID saleId);
}