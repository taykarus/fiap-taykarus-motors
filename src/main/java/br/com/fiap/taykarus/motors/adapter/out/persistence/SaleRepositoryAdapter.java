package br.com.fiap.taykarus.motors.adapter.out.persistence;

import br.com.fiap.taykarus.motors.adapter.out.persistence.entity.SaleEntity;
import br.com.fiap.taykarus.motors.adapter.out.persistence.repository.SpringDataSaleRepository;
import br.com.fiap.taykarus.motors.application.ports.out.SaleRepository;
import br.com.fiap.taykarus.motors.domain.common.Price;
import br.com.fiap.taykarus.motors.domain.sale.Cpf;
import br.com.fiap.taykarus.motors.domain.sale.Sale;
import br.com.fiap.taykarus.motors.domain.sale.SaleStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SaleRepositoryAdapter implements SaleRepository {

    private final SpringDataSaleRepository saleRepo;

    public SaleRepositoryAdapter(SpringDataSaleRepository saleRepo) {
        this.saleRepo = saleRepo;
    }

    @Override
    public Sale save(Sale sale) {
        SaleEntity entity = new SaleEntity(
                sale.getId(),
                sale.getVehicleId(),
                sale.getBuyerCpf().value(),
                sale.getSaleDate(),
                sale.getFinalPrice().value(),
                sale.getStatus().name()
        );

        saleRepo.save(entity);
        return sale;
    }

    @Override
    public Optional<Sale> findById(UUID id) {
        return saleRepo.findById(id)
                .map(entity -> new Sale(
                        entity.getId(),
                        entity.getVehicleId(),
                        new Cpf(entity.getBuyerCpf()),
                        entity.getSaleDate(),
                        new Price(entity.getFinalPrice()),
                        SaleStatus.valueOf(entity.getStatus())
                ));
    }
}