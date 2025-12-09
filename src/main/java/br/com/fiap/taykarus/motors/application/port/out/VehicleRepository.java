package br.com.fiap.taykarus.motors.application.port.out;

import br.com.fiap.taykarus.motors.domain.vehicle.Vehicle;
import br.com.fiap.taykarus.motors.domain.vehicle.VehicleStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository {
    Vehicle save(Vehicle vehicle);

    Optional<Vehicle> findById(UUID id);

    void delete(UUID id);

    List<Vehicle> findAllByStatusOrderByPriceAsc(VehicleStatus status);
}