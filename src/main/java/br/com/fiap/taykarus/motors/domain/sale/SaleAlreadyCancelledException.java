package br.com.fiap.taykarus.motors.domain.sale;

public class SaleAlreadyCancelledException extends IllegalStateException {
    public SaleAlreadyCancelledException(String message) {
        super(message);
    }
}
