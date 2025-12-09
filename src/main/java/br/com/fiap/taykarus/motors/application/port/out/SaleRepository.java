package br.com.fiap.taykarus.motors.application.port.out;

import br.com.fiap.taykarus.motors.domain.sale.Sale;

import java.util.Optional;
import java.util.UUID;

public interface SaleRepository {
    Sale save(Sale sale);

    Optional<Sale> findById(UUID id);
}