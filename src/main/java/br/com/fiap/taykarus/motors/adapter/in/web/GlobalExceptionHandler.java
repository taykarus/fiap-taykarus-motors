package br.com.fiap.taykarus.motors.adapter.in.web;

import br.com.fiap.taykarus.motors.domain.sale.SaleAlreadyCancelledException;
import br.com.fiap.taykarus.motors.domain.sale.SaleAlreadyConfirmedException;
import br.com.fiap.taykarus.motors.domain.vehicle.VehicleAlreadySoldException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(VehicleAlreadySoldException.class)
    public ResponseEntity<Object> handleVehicleSold(VehicleAlreadySoldException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler({SaleAlreadyConfirmedException.class, SaleAlreadyCancelledException.class})
    public ResponseEntity<Object> handleSaleStateConflict(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
    }
}