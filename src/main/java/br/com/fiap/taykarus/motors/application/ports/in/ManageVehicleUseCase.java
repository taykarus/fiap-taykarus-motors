package br.com.fiap.taykarus.motors.application.ports.in;

import br.com.fiap.taykarus.motors.application.ports.in.dto.EditVehicleCommand;
import br.com.fiap.taykarus.motors.application.ports.in.dto.RegisterVehicleCommand;
import br.com.fiap.taykarus.motors.application.ports.in.dto.VehicleDTO;

import java.util.List;
import java.util.UUID;

public interface ManageVehicleUseCase {
    VehicleDTO register(RegisterVehicleCommand command);

    VehicleDTO edit(EditVehicleCommand command);

    void delete(UUID id);

    List<VehicleDTO> listVehiclesForSale();

    List<VehicleDTO> listSoldVehicles();
}