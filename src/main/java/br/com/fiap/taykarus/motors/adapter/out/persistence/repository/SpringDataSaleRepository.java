package br.com.fiap.taykarus.motors.adapter.out.persistence.repository;

import br.com.fiap.taykarus.motors.adapter.out.persistence.entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataSaleRepository extends JpaRepository<SaleEntity, UUID> {
}