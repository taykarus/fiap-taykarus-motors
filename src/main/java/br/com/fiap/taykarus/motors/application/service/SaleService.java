package br.com.fiap.taykarus.motors.application.service;

import br.com.fiap.taykarus.motors.application.ports.in.SaleUseCase;
import br.com.fiap.taykarus.motors.application.ports.in.dto.SellVehicleCommand;
import br.com.fiap.taykarus.motors.application.ports.out.SaleRepository;
import br.com.fiap.taykarus.motors.application.ports.out.VehicleRepository;
import br.com.fiap.taykarus.motors.domain.common.Price;
import br.com.fiap.taykarus.motors.domain.sale.Cpf;
import br.com.fiap.taykarus.motors.domain.sale.Sale;
import br.com.fiap.taykarus.motors.domain.vehicle.Vehicle;

import java.util.UUID;

public class SaleService implements SaleUseCase {

    private final VehicleRepository vehicleRepository;
    private final SaleRepository saleRepository;

    public SaleService(VehicleRepository vehicleRepository, SaleRepository saleRepository) {
        this.vehicleRepository = vehicleRepository;
        this.saleRepository = saleRepository;
    }

    @Override
    public UUID sellVehicle(SellVehicleCommand command) {
        Vehicle vehicle = vehicleRepository.findById(command.vehicleId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        Cpf buyer = new Cpf(command.buyerCpf());

        Price salePrice = vehicle.getPrice();

        Sale sale = new Sale(vehicle, buyer, salePrice);

        vehicle.markAsSold();

        saleRepository.save(sale);
        vehicleRepository.save(vehicle);

        return sale.getId();
    }

    @Override
    public void confirmSale(UUID saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found"));

        sale.confirm();

        saleRepository.save(sale);
    }

    @Override
    public void cancelSale(UUID saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found"));

        Vehicle vehicle = vehicleRepository.findById(sale.getVehicleId())
                .orElseThrow(() -> new IllegalStateException("Associated vehicle not found"));

        sale.cancel();
        vehicle.markAvailable();

        saleRepository.save(sale);
        vehicleRepository.save(vehicle);
    }
}