package br.com.fiap.taykarus.motors.application.service;

import br.com.fiap.taykarus.motors.application.ports.in.dto.EditVehicleCommand;
import br.com.fiap.taykarus.motors.application.ports.in.dto.RegisterVehicleCommand;
import br.com.fiap.taykarus.motors.application.ports.in.dto.VehicleDTO;
import br.com.fiap.taykarus.motors.application.ports.out.VehicleRepository;
import br.com.fiap.taykarus.motors.domain.common.Price;
import br.com.fiap.taykarus.motors.domain.vehicle.Color;
import br.com.fiap.taykarus.motors.domain.vehicle.ModelYear;
import br.com.fiap.taykarus.motors.domain.vehicle.Vehicle;
import br.com.fiap.taykarus.motors.domain.vehicle.VehicleStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    @DisplayName("Should register a new vehicle successfully")
    void shouldRegisterVehicle() {
        // Arrange
        RegisterVehicleCommand command = new RegisterVehicleCommand(
                "Toyota", "Corolla", 2022, "White", new BigDecimal("120000")
        );

        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        VehicleDTO result = vehicleService.register(command);

        // Assert
        assertNotNull(result);
        assertEquals("Toyota", result.brand());
        assertEquals("Corolla", result.model());
        assertEquals(2022, result.year());
        assertEquals("White", result.color());
        assertEquals(new BigDecimal("120000"), result.price());
        assertEquals("FOR_SALE", result.status());

        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Should edit an existing vehicle")
    void shouldEditVehicle() {
        // Arrange
        UUID id = UUID.randomUUID();
        Vehicle existingVehicle = new Vehicle(
                "OldBrand", "OldModel", new ModelYear(2020), new Color("OldColor"), new Price(BigDecimal.TEN)
        );

        when(vehicleRepository.findById(id)).thenReturn(Optional.of(existingVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(i -> i.getArguments()[0]);

        EditVehicleCommand command = new EditVehicleCommand(
                id, "NewBrand", null, null, "NewColor", null
        );

        // Act
        VehicleDTO result = vehicleService.edit(command);

        // Assert
        assertEquals("NewBrand", result.brand());
        assertEquals("NewColor", result.color());
        assertEquals("OldModel", result.model());

        verify(vehicleRepository).save(existingVehicle);
    }

    @Test
    @DisplayName("Should throw exception when editing non-existent vehicle")
    void shouldThrowWhenEditingNonExistent() {
        UUID id = UUID.randomUUID();
        when(vehicleRepository.findById(id)).thenReturn(Optional.empty());

        EditVehicleCommand command = new EditVehicleCommand(
                id, "Brand", "Model", 2022, "Color", BigDecimal.TEN
        );

        assertThrows(IllegalArgumentException.class, () -> vehicleService.edit(command));
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete a vehicle")
    void shouldDeleteVehicle() {
        UUID id = UUID.randomUUID();
        Vehicle vehicle = new Vehicle("Brand", "Model", new ModelYear(2022), new Color("Red"), new Price(BigDecimal.TEN));

        when(vehicleRepository.findById(id)).thenReturn(Optional.of(vehicle));

        vehicleService.delete(id);

        verify(vehicleRepository, times(1)).delete(id);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent vehicle")
    void shouldThrowWhenDeletingNonExistent() {
        UUID id = UUID.randomUUID();
        when(vehicleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> vehicleService.delete(id));
        verify(vehicleRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should list vehicles for sale ordered by price")
    void shouldListVehiclesForSale() {
        Vehicle v1 = new Vehicle("A", "M", new ModelYear(2020), new Color("C"), new Price(BigDecimal.TEN));
        Vehicle v2 = new Vehicle("B", "M", new ModelYear(2020), new Color("C"), new Price(BigDecimal.ONE));

        when(vehicleRepository.findAllByStatusOrderByPriceAsc(VehicleStatus.FOR_SALE))
                .thenReturn(List.of(v2, v1));

        List<VehicleDTO> result = vehicleService.listVehiclesForSale();

        assertEquals(2, result.size());
        assertEquals(BigDecimal.ONE, result.get(0).price());
        assertEquals(BigDecimal.TEN, result.get(1).price());
        verify(vehicleRepository).findAllByStatusOrderByPriceAsc(VehicleStatus.FOR_SALE);
    }

    @Test
    @DisplayName("Should list sold vehicles ordered by price")
    void shouldListSoldVehicles() {
        when(vehicleRepository.findAllByStatusOrderByPriceAsc(VehicleStatus.SOLD))
                .thenReturn(List.of());

        List<VehicleDTO> result = vehicleService.listSoldVehicles();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(vehicleRepository).findAllByStatusOrderByPriceAsc(VehicleStatus.SOLD);
    }
}