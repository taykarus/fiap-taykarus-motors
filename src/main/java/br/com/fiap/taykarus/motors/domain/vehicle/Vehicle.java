package br.com.fiap.taykarus.motors.domain.vehicle;

import br.com.fiap.taykarus.motors.domain.common.ModificationLog;
import br.com.fiap.taykarus.motors.domain.common.Price;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public class Vehicle {

    private final UUID id;
    private String brand;
    private String model;
    private ModelYear year;
    private Color color;
    private Price price;
    private VehicleStatus status;
    private final List<ModificationLog> history;

    public Vehicle(String brand, String model, ModelYear year, Color color, Price price) {
        this.id = UUID.randomUUID();
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.price = price;
        this.status = VehicleStatus.FOR_SALE;
        this.history = new ArrayList<>();

        this.recordModification("CREATED");
    }

    public Vehicle(UUID id, String brand, String model, ModelYear year, Color color, Price price, VehicleStatus status, List<ModificationLog> history) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.price = price;
        this.status = status;
        this.history = history != null ? new ArrayList<>(history) : new ArrayList<>();
    }

    public void updateBrand(String newBrand) {
        validateActive();
        if (newBrand != null && !newBrand.equals(this.brand)) {
            this.brand = newBrand;
            this.recordModification("UPDATED_BRAND");
        }
    }

    public void updateModel(String newModel) {
        validateActive();
        if (newModel != null && !newModel.equals(this.model)) {
            this.model = newModel;
            this.recordModification("UPDATED_MODEL");
        }
    }

    public void updateYear(ModelYear newYear) {
        validateActive();
        if (newYear != null && !newYear.equals(this.year)) {
            this.year = newYear;
            this.recordModification("UPDATED_YEAR");
        }
    }

    public void updateColor(Color newColor) {
        validateActive();
        if (newColor != null && !newColor.equals(this.color)) {
            this.color = newColor;
            this.recordModification("UPDATED_COLOR");
        }
    }

    public void updatePrice(Price newPrice) {
        validateActive();
        if (newPrice != null && !newPrice.equals(this.price)) {
            this.price = newPrice;
            this.recordModification("UPDATED_PRICE");
        }
    }

    public void markAsSold() {
        if (this.status == VehicleStatus.SOLD) {
            throw new VehicleAlreadySoldException("Vehicle is already marked as sold.");
        }

        this.status = VehicleStatus.SOLD;
        this.recordModification("STATUS_CHANGED_TO_SOLD");
    }

    public void markAvailable() {
        if (this.status == VehicleStatus.FOR_SALE) return;

        this.status = VehicleStatus.FOR_SALE;
        this.recordModification("STATUS_CHANGED_TO_FOR_SALE");
    }

    private void validateActive() {
        if (this.status == VehicleStatus.SOLD) {
            throw new VehicleAlreadySoldException("Cannot modify a vehicle that has already been sold.");
        }
    }

    private void recordModification(String action) {
        this.history.add(new ModificationLog(
            this.id,
            "VEHICLE",
            LocalDateTime.now(),
            action
        ));
    }

    public List<ModificationLog> getHistory() {
        return Collections.unmodifiableList(history);
    }
}