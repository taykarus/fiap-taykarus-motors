package br.com.fiap.taykarus.motors.domain.vehicle;

public record ModelYear(int value) {
    public ModelYear {
        int currentYear = java.time.Year.now().getValue();
        if (value < 1900 || value > currentYear + 1) {
            throw new IllegalArgumentException("Invalid model year");
        }
    }
}