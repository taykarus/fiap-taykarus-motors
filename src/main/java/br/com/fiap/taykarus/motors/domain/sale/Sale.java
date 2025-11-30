package br.com.fiap.taykarus.motors.domain.sale;

import br.com.fiap.taykarus.motors.domain.common.Price;
import br.com.fiap.taykarus.motors.domain.vehicle.Vehicle;
import br.com.fiap.taykarus.motors.domain.vehicle.VehicleStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Sale {
    private final UUID id;
    private final UUID vehicleId;
    private final Cpf buyerCpf;
    private final LocalDateTime saleDate;
    private final Price finalPrice;
    private SaleStatus status;

    public Sale(Vehicle vehicle, Cpf buyerCpf, Price finalPrice) {
        if (vehicle == null || buyerCpf == null || finalPrice == null) {
            throw new IllegalArgumentException("Sale requires a valid Vehicle instance, Buyer CPF, and Price");
        }

        if (vehicle.getStatus() == VehicleStatus.SOLD) {
            throw new IllegalStateException("Cannot create a sale for a vehicle that is already sold");
        }

        this.id = UUID.randomUUID();
        this.vehicleId = vehicle.getId();
        this.buyerCpf = buyerCpf;
        this.finalPrice = finalPrice;
        this.saleDate = LocalDateTime.now();
        this.status = SaleStatus.REGISTERED;
    }

    public Sale(UUID id, UUID vehicleId, Cpf buyerCpf, LocalDateTime saleDate, Price finalPrice, SaleStatus status) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.buyerCpf = buyerCpf;
        this.saleDate = saleDate;
        this.finalPrice = finalPrice;
        this.status = status;
    }

    public void confirm() {
        if (this.status == SaleStatus.CANCELED) {
            throw new SaleAlreadyCancelledException("Cannot confirm a canceled sale");
        }
        this.status = SaleStatus.CONFIRMED;
    }

    public void cancel() {
        if (this.status == SaleStatus.CONFIRMED) {
            throw new SaleAlreadyConfirmedException("Cannot cancel a confirmed sale");
        }
        this.status = SaleStatus.CANCELED;
    }
}