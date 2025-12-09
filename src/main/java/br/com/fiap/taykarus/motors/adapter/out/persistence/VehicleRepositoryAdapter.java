package br.com.fiap.taykarus.motors.adapter.out.persistence;

import br.com.fiap.taykarus.motors.adapter.out.persistence.entity.AuditLogEntity;
import br.com.fiap.taykarus.motors.adapter.out.persistence.entity.VehicleEntity;
import br.com.fiap.taykarus.motors.adapter.out.persistence.repository.SpringDataLogRepository;
import br.com.fiap.taykarus.motors.adapter.out.persistence.repository.SpringDataVehicleRepository;
import br.com.fiap.taykarus.motors.application.port.out.VehicleRepository;
import br.com.fiap.taykarus.motors.domain.common.ModificationLog;
import br.com.fiap.taykarus.motors.domain.common.Price;
import br.com.fiap.taykarus.motors.domain.vehicle.Color;
import br.com.fiap.taykarus.motors.domain.vehicle.ModelYear;
import br.com.fiap.taykarus.motors.domain.vehicle.Vehicle;
import br.com.fiap.taykarus.motors.domain.vehicle.VehicleStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class VehicleRepositoryAdapter implements VehicleRepository {

    private final SpringDataVehicleRepository vehicleRepo;
    private final SpringDataLogRepository logRepo;

    public VehicleRepositoryAdapter(SpringDataVehicleRepository vehicleRepo, SpringDataLogRepository logRepo) {
        this.vehicleRepo = vehicleRepo;
        this.logRepo = logRepo;
    }

    @Override
    @Transactional
    public Vehicle save(Vehicle vehicle) {
        VehicleEntity entity = new VehicleEntity(
                vehicle.getId(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getYear().value(),
                vehicle.getColor().value(),
                vehicle.getPrice().value(),
                vehicle.getStatus().name()
        );
        vehicleRepo.save(entity);

        List<AuditLogEntity> logEntities = vehicle.getHistory().stream()
                .map(log -> new AuditLogEntity(
                        log.entityId(),
                        log.entityType(),
                        log.timestamp(),
                        log.action()
                ))
                .collect(Collectors.toList());

        logRepo.saveAll(logEntities);

        return vehicle;
    }

    @Override
    public Optional<Vehicle> findById(UUID id) {
        Optional<VehicleEntity> entityOpt = vehicleRepo.findById(id);

        if (entityOpt.isEmpty()) {
            return Optional.empty();
        }

        VehicleEntity entity = entityOpt.get();

        List<ModificationLog> history = logRepo.findByEntityIdOrderByTimestampAsc(id).stream()
                .map(log -> new ModificationLog(
                        log.getEntityId(),
                        log.getEntityType(),
                        log.getTimestamp(),
                        log.getAction()
                ))
                .collect(Collectors.toList());

        Vehicle vehicle = new Vehicle(
                entity.getId(),
                entity.getBrand(),
                entity.getModel(),
                new ModelYear(entity.getYear()),
                new Color(entity.getColor()),
                new Price(entity.getPrice()),
                VehicleStatus.valueOf(entity.getStatus()),
                history
        );

        return Optional.of(vehicle);
    }

    @Override
    public void delete(UUID id) {
        vehicleRepo.deleteById(id);
    }

    @Override
    public List<Vehicle> findAllByStatusOrderByPriceAsc(VehicleStatus status) {
        List<VehicleEntity> entities = vehicleRepo.findAllByStatusOrderByPriceAsc(status.name());

        return entities.stream()
                .map(entity -> {
                    List<ModificationLog> history = logRepo.findByEntityIdOrderByTimestampAsc(entity.getId())
                            .stream()
                            .map(l -> new ModificationLog(l.getEntityId(), l.getEntityType(), l.getTimestamp(), l.getAction()))
                            .collect(Collectors.toList());

                    return new Vehicle(
                            entity.getId(),
                            entity.getBrand(),
                            entity.getModel(),
                            new ModelYear(entity.getYear()),
                            new Color(entity.getColor()),
                            new Price(entity.getPrice()),
                            VehicleStatus.valueOf(entity.getStatus()),
                            history
                    );
                })
                .collect(Collectors.toList());
    }
}