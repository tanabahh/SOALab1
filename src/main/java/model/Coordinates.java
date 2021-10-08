package model;

import com.sun.istack.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Coordinates {

    @Id
    //@SequenceGenerator(name="coordinate-gen",sequenceName="COORDINATE_GEN", initialValue=0, allocationSize=12)
    @GeneratedValue(strategy= GenerationType.IDENTITY)//, generator="coordinate-gen")
    Long id;

    @NotNull
    int x; //Поле не может быть null

    @Min(-647)
    int y; //Значение поля должно быть больше -647

    public Coordinates() {}
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Long getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}