package model;

import java.io.Serializable;
import java.time.LocalDate;

public class VehicleDto implements Serializable {
    private int id;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public long getEnginePower() {
        return enginePower;
    }

    public String getType() {
        return type;
    }

    public String getFuelType() {
        return fuelType;
    }

    private String name;
    private int x;
    private int y;
    private LocalDate creationDate;
    private long enginePower;
    private String type;
    private String fuelType;

    public VehicleDto(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.name = vehicle.getName();
        this.x = vehicle.getCoordinates().getX();
        this.y = vehicle.getCoordinates().getY();
        this.creationDate = vehicle.getCreationDate();
        this.enginePower = vehicle.getEnginePower();
        this.type = vehicle.getType().name();
        this.fuelType = vehicle.getFuelType().name();
    }
}
