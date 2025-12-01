package br.com.fiap.taykarus.motors.domain.sale;

import br.com.fiap.taykarus.motors.domain.common.Price;
import br.com.fiap.taykarus.motors.domain.vehicle.Color;
import br.com.fiap.taykarus.motors.domain.vehicle.ModelYear;
import br.com.fiap.taykarus.motors.domain.vehicle.Vehicle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SaleTest {

    @Test
    @DisplayName("Should create a valid sale for a FOR_SALE vehicle")
    void shouldCreateValidSale() {
        Vehicle vehicle = createAvailableVehicle();
        Cpf buyerCpf = new Cpf("123.456.789-00");
        Price salePrice = new Price(new BigDecimal("85000"));

        Sale sale = new Sale(vehicle, buyerCpf, salePrice);

        assertNotNull(sale.getId());
        assertEquals(vehicle.getId(), sale.getVehicleId()); // Checks ID reference
        assertEquals(SaleStatus.REGISTERED, sale.getStatus());
        assertNotNull(sale.getSaleDate());
    }

    @Test
    @DisplayName("Should reject null values in business constructor")
    void shouldRejectNullsInConstructor() {
        Vehicle vehicle = createAvailableVehicle();
        Cpf cpf = new Cpf("12345678900");
        Price price = new Price(BigDecimal.TEN);

        // 1. Null Vehicle
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () ->
                new Sale(null, cpf, price)
        );
        assertEquals("Sale requires a valid Vehicle instance, Buyer CPF, and Price", ex1.getMessage());

        // 2. Null CPF
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () ->
                new Sale(vehicle, null, price)
        );
        assertEquals("Sale requires a valid Vehicle instance, Buyer CPF, and Price", ex2.getMessage());

        // 3. Null Price
        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, () ->
                new Sale(vehicle, cpf, null)
        );
        assertEquals("Sale requires a valid Vehicle instance, Buyer CPF, and Price", ex3.getMessage());
    }

    @Test
    @DisplayName("Should reconstitute sale from persistence (all-args constructor)")
    void shouldReconstituteSale() {
        // Arrange
        UUID fixedId = UUID.randomUUID();
        UUID fixedVehicleId = UUID.randomUUID();
        Cpf fixedCpf = new Cpf("12345678900");
        LocalDateTime fixedDate = LocalDateTime.of(2023, 1, 1, 12, 0);
        Price fixedPrice = new Price(new BigDecimal("50000"));
        SaleStatus fixedStatus = SaleStatus.CONFIRMED;

        // Act
        Sale sale = new Sale(
                fixedId,
                fixedVehicleId,
                fixedCpf,
                fixedDate,
                fixedPrice,
                fixedStatus
        );

        // Assert
        assertEquals(fixedId, sale.getId());
        assertEquals(fixedVehicleId, sale.getVehicleId());
        assertEquals(fixedCpf, sale.getBuyerCpf());
        assertEquals(fixedDate, sale.getSaleDate());
        assertEquals(fixedPrice, sale.getFinalPrice());
        assertEquals(fixedStatus, sale.getStatus());
    }

    @Test
    @DisplayName("Should fail to create sale if vehicle is already SOLD")
    void shouldPreventSaleOfSoldVehicle() {
        Vehicle vehicle = createAvailableVehicle();
        vehicle.markAsSold(); // Vehicle is now unavailable

        Cpf buyerCpf = new Cpf("123.456.789-00");
        Price salePrice = new Price(new BigDecimal("85000"));

        assertThrows(IllegalStateException.class, () -> {
            new Sale(vehicle, buyerCpf, salePrice);
        });
    }

    @Test
    @DisplayName("Should confirm a registered sale")
    void shouldConfirmSale() {
        Sale sale = createDefaultSale();

        sale.confirm();

        assertEquals(SaleStatus.CONFIRMED, sale.getStatus());
    }

    @Test
    @DisplayName("Should cancel a registered sale")
    void shouldCancelSale() {
        Sale sale = createDefaultSale();

        sale.cancel();

        assertEquals(SaleStatus.CANCELED, sale.getStatus());
    }

    @Test
    @DisplayName("Should not allow cancelling a confirmed sale")
    void shouldPreventCancellingConfirmedSale() {
        Sale sale = createDefaultSale();
        sale.confirm();

        assertThrows(SaleAlreadyConfirmedException.class, sale::cancel);
    }

    @Test
    @DisplayName("Should not allow confirming a cancelled sale")
    void shouldPreventConfirmingCancelledSale() {
        Sale sale = createDefaultSale();
        sale.cancel();

        assertThrows(SaleAlreadyCancelledException.class, sale::confirm);
    }

    // Helpers
    private Vehicle createAvailableVehicle() {
        return new Vehicle("Fiat", "Uno", new ModelYear(2010), new Color("Red"), new Price(new BigDecimal("20000")));
    }

    private Sale createDefaultSale() {
        return new Sale(createAvailableVehicle(), new Cpf("12345678900"), new Price(new BigDecimal("20000")));
    }
}