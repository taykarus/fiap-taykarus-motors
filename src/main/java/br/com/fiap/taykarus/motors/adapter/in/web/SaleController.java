package br.com.fiap.taykarus.motors.adapter.in.web;

import br.com.fiap.taykarus.motors.application.port.in.SaleUseCase;
import br.com.fiap.taykarus.motors.application.port.in.dto.SellVehicleCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleUseCase saleUseCase;

    public SaleController(SaleUseCase saleUseCase) {
        this.saleUseCase = saleUseCase;
    }

    @PostMapping
    public ResponseEntity<UUID> sellVehicle(@RequestBody SellVehicleCommand command) {
        UUID saleId = saleUseCase.sellVehicle(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(saleId);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmSale(@PathVariable UUID id) {
        saleUseCase.confirmSale(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelSale(@PathVariable UUID id) {
        saleUseCase.cancelSale(id);
        return ResponseEntity.ok().build();
    }
}