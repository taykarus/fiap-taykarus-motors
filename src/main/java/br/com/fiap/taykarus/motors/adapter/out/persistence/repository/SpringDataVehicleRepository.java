package br.com.fiap.taykarus.motors.adapter.out.persistence.repository;

import br.com.fiap.taykarus.motors.adapter.out.persistence.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataVehicleRepository extends JpaRepository<VehicleEntity, UUID> {
    List<VehicleEntity> findAllByStatusOrderByPriceAsc(String status);
}