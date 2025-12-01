package br.com.fiap.taykarus.motors.domain.vehicle;

import br.com.fiap.taykarus.motors.domain.common.ModificationLog;
import br.com.fiap.taykarus.motors.domain.common.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    @Test
    @DisplayName("Should create a valid vehicle with initial FOR_SALE status and creation log")
    void shouldCreateValidVehicle() {
        Vehicle vehicle = new Vehicle(
                "Toyota",
                "Corolla",
                new ModelYear(2022),
                new Color("White"),
                new Price(new BigDecimal("120000.00"))
        );

        assertNotNull(vehicle.getId());
        assertEquals("Toyota", vehicle.getBrand());
        assertEquals("Corolla", vehicle.getModel());
        assertEquals(2022, vehicle.getYear().value());
        assertEquals("White", vehicle.getColor().value());
        assertEquals(new BigDecimal("120000.00"), vehicle.getPrice().value());
        assertEquals(VehicleStatus.FOR_SALE, vehicle.getStatus());

        // Verify Audit Log
        assertFalse(vehicle.getHistory().isEmpty());
        assertEquals("CREATED", vehicle.getHistory().getFirst().action());
    }

    @Test
    @DisplayName("Should reconstitute vehicle from persistence (all-args constructor)")
    void shouldReconstituteVehicle() {
        // Arrange: Simulate data coming from a database
        UUID fixedId = UUID.randomUUID();
        ModificationLog oldLog = new ModificationLog(fixedId, "VEHICLE", LocalDateTime.now().minusDays(1), "CREATED");
        List<ModificationLog> existingHistory = List.of(oldLog);

        // Act: Use the persistence constructor
        Vehicle vehicle = new Vehicle(
                fixedId,
                "Ford",
                "Mustang",
                new ModelYear(1969),
                new Color("Red"),
                new Price(new BigDecimal("250000.00")),
                VehicleStatus.SOLD,
                existingHistory
        );

        // Assert: State must match exactly without generating new IDs or Logs
        assertEquals(fixedId, vehicle.getId());
        assertEquals("Ford", vehicle.getBrand());
        assertEquals("Mustang", vehicle.getModel());
        assertEquals(VehicleStatus.SOLD, vehicle.getStatus());
        assertEquals(1, vehicle.getHistory().size());
        assertEquals("CREATED", vehicle.getHistory().getFirst().action());
    }

    @Test
    @DisplayName("Should handle null history in persistence constructor by creating empty list")
    void shouldHandleNullHistoryInReconstitution() {
        UUID id = UUID.randomUUID();
        Vehicle vehicle = new Vehicle(
                id, "Brand", "Model", new ModelYear(2022), new Color("Blue"),
                new Price(BigDecimal.TEN), VehicleStatus.FOR_SALE, null
        );

        assertNotNull(vehicle.getHistory());
        assertTrue(vehicle.getHistory().isEmpty());
    }

    @Test
    @DisplayName("Should update vehicle brand and record history")
    void shouldUpdateBrand() {
        Vehicle vehicle = createDefaultVehicle();
        vehicle.updateBrand("Honda Updated");

        assertEquals("Honda Updated", vehicle.getBrand());
        assertLastAction(vehicle, "UPDATED_BRAND");
    }

    @Test
    @DisplayName("Should update vehicle model and record history")
    void shouldUpdateModel() {
        Vehicle vehicle = createDefaultVehicle();
        vehicle.updateModel("Civic Type R");

        assertEquals("Civic Type R", vehicle.getModel());
        assertLastAction(vehicle, "UPDATED_MODEL");
    }

    @Test
    @DisplayName("Should update vehicle year and record history")
    void shouldUpdateYear() {
        Vehicle vehicle = createDefaultVehicle();
        ModelYear newYear = new ModelYear(2022);
        vehicle.updateYear(newYear);

        assertEquals(newYear, vehicle.getYear());
        assertLastAction(vehicle, "UPDATED_YEAR");
    }

    @Test
    @DisplayName("Should update vehicle color and record history")
    void shouldUpdateColor() {
        Vehicle vehicle = createDefaultVehicle();
        Color newColor = new Color("Black");
        vehicle.updateColor(newColor);

        assertEquals(newColor, vehicle.getColor());
        assertLastAction(vehicle, "UPDATED_COLOR");
    }

    @Test
    @DisplayName("Should update vehicle price and record history")
    void shouldUpdatePrice() {
        Vehicle vehicle = createDefaultVehicle();
        Price newPrice = new Price(new BigDecimal("115000.00"));
        vehicle.updatePrice(newPrice);

        assertEquals(newPrice, vehicle.getPrice());
        assertLastAction(vehicle, "UPDATED_PRICE");
    }

    @Test
    @DisplayName("Should not record history if update value is the same")
    void shouldNotRecordHistoryIfValueSame() {
        Vehicle vehicle = createDefaultVehicle();
        int initialHistorySize = vehicle.getHistory().size();

        // Updating with same values
        vehicle.updateBrand(vehicle.getBrand());
        vehicle.updateColor(vehicle.getColor());

        assertEquals(initialHistorySize, vehicle.getHistory().size());
    }

    @Test
    @DisplayName("Should mark vehicle as SOLD and log the event")
    void shouldMarkAsSold() {
        Vehicle vehicle = createDefaultVehicle();

        vehicle.markAsSold();

        assertEquals(VehicleStatus.SOLD, vehicle.getStatus());
        assertLastAction(vehicle, "STATUS_CHANGED_TO_SOLD");
    }

    @Test
    @DisplayName("Should mark vehicle as AVAILABLE (For Sale) and log the event")
    void shouldMarkAvailable() {
        Vehicle vehicle = createDefaultVehicle();
        vehicle.markAsSold(); // First sell it

        vehicle.markAvailable(); // Then revert

        assertEquals(VehicleStatus.FOR_SALE, vehicle.getStatus());
        assertLastAction(vehicle, "STATUS_CHANGED_TO_FOR_SALE");
    }

    @Test
    @DisplayName("Should do nothing if marking available when already available")
    void shouldDoNothingWhenMarkingAvailableIfAlreadyAvailable() {
        Vehicle vehicle = createDefaultVehicle();
        int initialHistorySize = vehicle.getHistory().size();

        vehicle.markAvailable();

        assertEquals(VehicleStatus.FOR_SALE, vehicle.getStatus());
        assertEquals(initialHistorySize, vehicle.getHistory().size());
    }

    @Test
    @DisplayName("Should prevent ANY updates on a SOLD vehicle")
    void shouldPreventUpdatesWhenSold() {
        Vehicle vehicle = createDefaultVehicle();
        vehicle.markAsSold();

        assertThrows(VehicleAlreadySoldException.class, () -> vehicle.updateBrand("New Brand"));
        assertThrows(VehicleAlreadySoldException.class, () -> vehicle.updateModel("New Model"));
        assertThrows(VehicleAlreadySoldException.class, () -> vehicle.updateYear(new ModelYear(2023)));
        assertThrows(VehicleAlreadySoldException.class, () -> vehicle.updateColor(new Color("Blue")));
        assertThrows(VehicleAlreadySoldException.class, () -> vehicle.updatePrice(new Price(BigDecimal.TEN)));
    }

    @Test
    @DisplayName("Should fail when trying to sell an already sold vehicle")
    void shouldFailMarkingSoldTwice() {
        Vehicle vehicle = createDefaultVehicle();
        vehicle.markAsSold();

        VehicleAlreadySoldException exception = assertThrows(VehicleAlreadySoldException.class, vehicle::markAsSold);
        assertEquals("Vehicle is already marked as sold.", exception.getMessage());
    }

    // --- Helpers ---

    private Vehicle createDefaultVehicle() {
        return new Vehicle(
                "Honda",
                "Civic",
                new ModelYear(2021),
                new Color("Silver"),
                new Price(new BigDecimal("90000"))
        );
    }

    private void assertLastAction(Vehicle vehicle, String expectedAction) {
        String lastAction = vehicle.getHistory().getLast().action();
        assertEquals(expectedAction, lastAction);
    }
}