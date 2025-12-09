package br.com.fiap.taykarus.motors.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleEntity {

    @Id
    private UUID id;

    private UUID vehicleId;
    private String buyerCpf;
    private LocalDateTime saleDate;
    private BigDecimal finalPrice;
    private String status;
}