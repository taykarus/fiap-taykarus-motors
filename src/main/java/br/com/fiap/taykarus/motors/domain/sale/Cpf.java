package br.com.fiap.taykarus.motors.domain.sale;

import java.util.regex.Pattern;

public record Cpf(String value) {
    private static final Pattern ONLY_NUMBERS = Pattern.compile("\\d{11}");

    public Cpf {
        if (value == null) throw new IllegalArgumentException("CPF cannot be null");

        value = value.replaceAll("\\D", "");

        if (!ONLY_NUMBERS.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid CPF format");
        }
    }
}