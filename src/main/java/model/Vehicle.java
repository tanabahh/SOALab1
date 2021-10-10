package model;

import exception.BadRequestException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    //@SequenceGenerator(name="vehicle-gen",sequenceName="VEHICLE_GEN", initialValue=0, allocationSize=12)
    @GeneratedValue(strategy= GenerationType.IDENTITY)//, generator="vehicle-gen")
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @NotNull
    private String name; //Поле не может быть null, Строка не может быть пустой

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Coordinates coordinates; //Поле не может быть null

    @NotNull
    @Column(name = "creation_date", columnDefinition= "TIMESTAMP WITH TIME ZONE")
    private LocalDate creationDate = LocalDate.now(); //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @Column(name = "engine_power")
    @Min(value = 1)
    private long enginePower; //Значение поля должно быть больше 0

    @Enumerated(EnumType.STRING)
    private VehicleType type; //Поле может быть null
    @Column(name = "fuel_type")
    @Enumerated(EnumType.STRING)
    @NotNull
    private FuelType fuelType; //Поле не может быть null

    public Vehicle() {}

    public Vehicle(String name, Coordinates coordinates,
        long enginePower,
        VehicleType type, FuelType fuelType) throws BadRequestException{
        validator(name, coordinates, enginePower, type, fuelType);
        this.name = name;
        this.coordinates = coordinates;
        this.enginePower = enginePower;
        this.type = type;
        this.fuelType = fuelType;
    }

    public void setName(String name) throws BadRequestException {
        if (StringUtils.isEmpty(name)) {
            throw new BadRequestException("name should be not empty");
        }
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) throws BadRequestException {
        if (coordinates == null) throw
            new BadRequestException("coordinates should be not empty");
        this.coordinates = coordinates;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public void setEnginePower(long enginePower) throws BadRequestException {
        if (enginePower <= 0) throw new BadRequestException("enginePower should be > 0");
        this.enginePower = enginePower;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public void setFuelType(FuelType fuelType) throws BadRequestException {
        if (fuelType == null) {
            throw new BadRequestException("fuelType should be != null");
        }
        this.fuelType = fuelType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public long getEnginePower() {
        return enginePower;
    }

    public VehicleType getType() {
        return type;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    private void validator(String name, Coordinates coordinates,
        long enginePower,
        VehicleType type, FuelType fuelType) throws BadRequestException {
        if (StringUtils.isEmpty(name)) {
            throw new BadRequestException("name should be not empty");
        }
        if (coordinates == null) {
            throw new BadRequestException("coordinates should be not empty");
        }
        if (enginePower <= 0) {
            throw new BadRequestException("enginePower should be > 0");
        }
        if (fuelType == null) {
            throw new BadRequestException("fuelType should be != null");
        }
    }
}
