package br.com.fiap.taykarus.motors.adapter.out.persistence.repository;

import br.com.fiap.taykarus.motors.adapter.out.persistence.entity.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataLogRepository extends JpaRepository<AuditLogEntity, UUID> {
    List<AuditLogEntity> findByEntityIdOrderByTimestampAsc(UUID entityId);
}