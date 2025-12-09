package br.com.fiap.taykarus.motors.application.service;

import br.com.fiap.taykarus.motors.application.port.in.ManageVehicleUseCase;
import br.com.fiap.taykarus.motors.application.port.in.dto.EditVehicleCommand;
import br.com.fiap.taykarus.motors.application.port.in.dto.RegisterVehicleCommand;
import br.com.fiap.taykarus.motors.application.port.in.dto.VehicleDTO;
import br.com.fiap.taykarus.motors.application.port.out.VehicleRepository;
import br.com.fiap.taykarus.motors.domain.common.Price;
import br.com.fiap.taykarus.motors.domain.vehicle.Color;
import br.com.fiap.taykarus.motors.domain.vehicle.ModelYear;
import br.com.fiap.taykarus.motors.domain.vehicle.Vehicle;
import br.com.fiap.taykarus.motors.domain.vehicle.VehicleStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class VehicleService implements ManageVehicleUseCase {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public VehicleDTO register(RegisterVehicleCommand command) {
        Vehicle newVehicle = new Vehicle(
                command.brand(),
                command.model(),
                new ModelYear(command.year()),
                new Color(command.color()),
                new Price(command.price())
        );

        Vehicle saved = vehicleRepository.save(newVehicle);

        return mapToDTO(saved);
    }

    @Override
    public VehicleDTO edit(EditVehicleCommand command) {
        Vehicle vehicle = vehicleRepository.findById(command.id())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        if (command.brand() != null) vehicle.updateBrand(command.brand());
        if (command.model() != null) vehicle.updateModel(command.model());
        if (command.year() != null) vehicle.updateYear(new ModelYear(command.year()));
        if (command.color() != null) vehicle.updateColor(new Color(command.color()));
        if (command.price() != null) vehicle.updatePrice(new Price(command.price()));

        Vehicle saved = vehicleRepository.save(vehicle);

        return mapToDTO(saved);
    }

    @Override
    public void delete(UUID id) {
        if (vehicleRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Vehicle not found");
        }
        vehicleRepository.delete(id);
    }

    @Override
    public List<VehicleDTO> listVehiclesForSale() {
        return vehicleRepository.findAllByStatusOrderByPriceAsc(VehicleStatus.FOR_SALE)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleDTO> listSoldVehicles() {
        return vehicleRepository.findAllByStatusOrderByPriceAsc(VehicleStatus.SOLD)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private VehicleDTO mapToDTO(Vehicle v) {
        LocalDateTime created = v.getHistory().isEmpty() ? LocalDateTime.now() : v.getHistory().getFirst().timestamp();

        return new VehicleDTO(
                v.getId(),
                v.getBrand(),
                v.getModel(),
                v.getYear().value(),
                v.getColor().value(),
                v.getPrice().value(),
                v.getStatus().name(),
                created
        );
    }
}