package br.com.fiap.taykarus.motors.domain.vehicle;

import br.com.fiap.taykarus.motors.domain.common.Price;
import lombok.Getter;

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

    public Vehicle(String brand, String model, ModelYear year, Color color, Price price) {
        this.id = UUID.randomUUID();
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.price = price;
        this.status = VehicleStatus.FOR_SALE;
    }

    public Vehicle(UUID id, String brand, String model, ModelYear year, Color color, Price price, VehicleStatus status) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.price = price;
        this.status = status;
    }

    public void updateBrand(String newBrand) {
        validateActive();
        this.brand = newBrand;
    }

    public void updateModel(String newModel) {
        validateActive();
        this.model = newModel;
    }

    public void updateYear(ModelYear newYear) {
        validateActive();
        this.year = newYear;
    }

    public void updateColor(Color newColor) {
        validateActive();
        this.color = newColor;
    }

    public void updatePrice(Price newPrice) {
        validateActive();
        this.price = newPrice;
    }

    public void markAsSold() {
        validateActive();
        this.status = VehicleStatus.SOLD;
    }

    public void markAvailable() {
        if (this.status == VehicleStatus.FOR_SALE) return;

        this.status = VehicleStatus.FOR_SALE;
    }

    private void validateActive() {
        if (this.status == VehicleStatus.SOLD) {
            throw new VehicleAlreadySoldException("Cannot modify a vehicle that has already been sold.");
        }
    }
}