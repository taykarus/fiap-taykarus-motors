package br.com.fiap.taykarus.motors.application.service;

import br.com.fiap.taykarus.motors.application.ports.in.dto.SellVehicleCommand;
import br.com.fiap.taykarus.motors.application.ports.out.SaleRepository;
import br.com.fiap.taykarus.motors.application.ports.out.VehicleRepository;
import br.com.fiap.taykarus.motors.domain.common.Price;
import br.com.fiap.taykarus.motors.domain.sale.Sale;
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
class SellServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private SaleRepository saleRepository;

    @InjectMocks
    private SellService sellService;

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
        UUID saleId = sellService.sellVehicle(command);

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
                () -> sellService.sellVehicle(command));

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
        assertThrows(IllegalStateException.class, () -> sellService.sellVehicle(command));

        verify(saleRepository, never()).save(any());
    }
}