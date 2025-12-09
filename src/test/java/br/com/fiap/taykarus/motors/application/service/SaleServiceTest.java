package br.com.fiap.taykarus.motors.application.service;

import br.com.fiap.taykarus.motors.application.port.in.dto.SellVehicleCommand;
import br.com.fiap.taykarus.motors.application.port.out.SaleRepository;
import br.com.fiap.taykarus.motors.application.port.out.VehicleRepository;
import br.com.fiap.taykarus.motors.domain.common.Price;
import br.com.fiap.taykarus.motors.domain.sale.Cpf;
import br.com.fiap.taykarus.motors.domain.sale.Sale;
import br.com.fiap.taykarus.motors.domain.sale.SaleStatus;
import br.com.fiap.taykarus.motors.domain.vehicle.Color;
import br.com.fiap.taykarus.motors.domain.vehicle.ModelYear;
import br.com.fiap.taykarus.motors.domain.vehicle.Vehicle;
import br.com.fiap.taykarus.motors.domain.vehicle.VehicleStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private SaleRepository saleRepository;

    @InjectMocks
    private SaleService saleService;

    @Test
    @DisplayName("Should sell a vehicle successfully")
    void shouldSellVehicle() {
        // Arrange
        UUID vehicleId = UUID.randomUUID();
        BigDecimal vehiclePrice = new BigDecimal("85000.00");
        Vehicle vehicle = new Vehicle(
                "Honda", "Civic", new ModelYear(2021), new Color("Silver"), new Price(vehiclePrice)
        );
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        SellVehicleCommand command = new SellVehicleCommand(vehicleId, "123.456.789-00");

        // Act
        UUID saleId = saleService.sellVehicle(command);

        // Assert 1: Verify Vehicle state changed
        assertEquals(VehicleStatus.SOLD, vehicle.getStatus());

        // Assert 2: Verify Repositories called
        verify(vehicleRepository).save(vehicle);

        // Assert 3: Capture the Sale object to check internal data
        ArgumentCaptor<Sale> saleCaptor = ArgumentCaptor.forClass(Sale.class);
        verify(saleRepository).save(saleCaptor.capture());

        Sale savedSale = saleCaptor.getValue();
        assertEquals(vehicle.getId(), savedSale.getVehicleId());
        assertEquals("12345678900", savedSale.getBuyerCpf().value());
        assertEquals(vehiclePrice, savedSale.getFinalPrice().value());
        assertNotNull(savedSale.getId());
        assertEquals(saleId, savedSale.getId());
    }

    @Test
    @DisplayName("Should throw exception if vehicle not found")
    void shouldThrowIfVehicleNotFound() {
        UUID vehicleId = UUID.randomUUID();
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        SellVehicleCommand command = new SellVehicleCommand(vehicleId, "123.456.789-00");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> saleService.sellVehicle(command));

        assertEquals("Vehicle not found", ex.getMessage());
        verify(saleRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception if vehicle is already sold (Business Rule from Domain)")
    void shouldThrowIfVehicleAlreadySold() {
        // Arrange
        UUID vehicleId = UUID.randomUUID();
        Vehicle vehicle = new Vehicle("Brand", "Model", new ModelYear(2022), new Color("Red"), new Price(BigDecimal.TEN));
        vehicle.markAsSold();

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        SellVehicleCommand command = new SellVehicleCommand(vehicleId, "123.456.789-00");

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> saleService.sellVehicle(command));

        verify(saleRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should confirm a sale successfully")
    void shouldConfirmSale() {
        // Arrange
        UUID saleId = UUID.randomUUID();
        Vehicle vehicle = new Vehicle("Brand", "Model", new ModelYear(2022), new Color("Blue"), new Price(BigDecimal.TEN));
        Sale sale = new Sale(vehicle, new Cpf("12345678900"), new Price(BigDecimal.TEN));

        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));

        // Act
        saleService.confirmSale(saleId);

        // Assert
        assertEquals(SaleStatus.CONFIRMED, sale.getStatus());
        verify(saleRepository).save(sale);
    }

    @Test
    @DisplayName("Should throw exception when confirming non-existent sale")
    void shouldThrowWhenConfirmingNonExistentSale() {
        UUID saleId = UUID.randomUUID();
        when(saleRepository.findById(saleId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> saleService.confirmSale(saleId));
        verify(saleRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should cancel a sale and revert vehicle inventory")
    void shouldCancelSale() {
        // Arrange
        UUID saleId = UUID.randomUUID();
        Vehicle vehicle = new Vehicle("Brand", "Model", new ModelYear(2022), new Color("Blue"), new Price(BigDecimal.TEN));

        Sale sale = new Sale(vehicle, new Cpf("12345678900"), new Price(BigDecimal.TEN));

        vehicle.markAsSold();

        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
        when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));

        // Act
        saleService.cancelSale(saleId);

        // Assert
        assertEquals(SaleStatus.CANCELED, sale.getStatus());
        assertEquals(VehicleStatus.FOR_SALE, vehicle.getStatus());

        verify(saleRepository).save(sale);
        verify(vehicleRepository).save(vehicle);
    }

    @Test
    @DisplayName("Should throw exception when canceling non-existent sale")
    void shouldThrowWhenCancelingNonExistentSale() {
        UUID saleId = UUID.randomUUID();
        when(saleRepository.findById(saleId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> saleService.cancelSale(saleId));
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when canceling sale if associated vehicle is missing")
    void shouldThrowWhenCancelingIfVehicleMissing() {
        // Arrange
        UUID saleId = UUID.randomUUID();
        Vehicle vehicle = new Vehicle("Brand", "Model", new ModelYear(2022), new Color("Blue"), new Price(BigDecimal.TEN));
        Sale sale = new Sale(vehicle, new Cpf("12345678900"), new Price(BigDecimal.TEN));

        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
        when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> saleService.cancelSale(saleId));
        verify(saleRepository, never()).save(any());
    }
}