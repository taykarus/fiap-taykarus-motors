package br.com.fiap.taykarus.motors.domain.sale;

public class SaleAlreadyConfirmedException extends IllegalStateException {
    public SaleAlreadyConfirmedException(String message) {
        super(message);
    }
}
