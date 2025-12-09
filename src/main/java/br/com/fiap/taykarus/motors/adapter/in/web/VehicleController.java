package br.com.fiap.taykarus.motors.adapter.in.web;

import br.com.fiap.taykarus.motors.application.port.in.ManageVehicleUseCase;
import br.com.fiap.taykarus.motors.application.port.in.dto.EditVehicleCommand;
import br.com.fiap.taykarus.motors.application.port.in.dto.RegisterVehicleCommand;
import br.com.fiap.taykarus.motors.application.port.in.dto.VehicleDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final ManageVehicleUseCase manageVehicleUseCase;

    public VehicleController(ManageVehicleUseCase manageVehicleUseCase) {
        this.manageVehicleUseCase = manageVehicleUseCase;
    }

    @PostMapping
    public ResponseEntity<VehicleDTO> register(@RequestBody RegisterVehicleCommand command) {
        VehicleDTO created = manageVehicleUseCase.register(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> edit(@PathVariable UUID id, @RequestBody EditVehicleCommand command) {
        EditVehicleCommand safeCommand = new EditVehicleCommand(
                id,
                command.brand(),
                command.model(),
                command.year(),
                command.color(),
                command.price()
        );
        return ResponseEntity.ok(manageVehicleUseCase.edit(safeCommand));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        manageVehicleUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/for-sale")
    public ResponseEntity<List<VehicleDTO>> listForSale() {
        return ResponseEntity.ok(manageVehicleUseCase.listVehiclesForSale());
    }

    @GetMapping("/sold")
    public ResponseEntity<List<VehicleDTO>> listSold() {
        return ResponseEntity.ok(manageVehicleUseCase.listSoldVehicles());
    }
}