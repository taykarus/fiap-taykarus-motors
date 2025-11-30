package br.com.fiap.taykarus.motors.domain.vehicle;

public class VehicleAlreadySoldException extends IllegalStateException {
    public VehicleAlreadySoldException(String message) {
        super(message);
    }
}
