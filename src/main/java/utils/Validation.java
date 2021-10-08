package utils;

import exception.BadRequestException;
import java.util.Arrays;
import model.FuelType;
import model.VehicleType;

public class Validation {

    public String checkNotEmptyString(String value, String paramName) throws BadRequestException {
        if (value == null || value.isEmpty()){
            throw new BadRequestException(String.format("%s должно быть не пустым", paramName));
        }
        return value;
    }

    public Integer checkPositiveIntOrNull(String value, String paramName) throws BadRequestException {
        if (value == null || value.isEmpty()) {
            return null;
        }
        Integer number;
        try {
            number = Integer.parseInt(value);
        }
        catch (Exception e) {
            throw new BadRequestException(String.format("%s должно быть числом", paramName));
        }
        if (number < 1) {
            throw new BadRequestException(String.format("%s должно быть >= 1", paramName));
        }
        return number;
    }

    public Integer checkInteger(String value,  String paramName) throws BadRequestException {
        Integer number;
        try {
            number = Integer.parseInt(value);
        }
        catch (Exception e) {
            throw new BadRequestException(String.format("%s должно быть числом", paramName));
        }
        return number;
    }

    public Integer checkIntegerNotNull(String value, String paramName) throws BadRequestException {
        Integer number;
        try {
            number = Integer.parseInt(value);
        }
        catch (Exception e) {
            throw new BadRequestException(String.format("%s должно быть числом", paramName));
        }
        if (number == null) {
            throw new BadRequestException(String.format("%s должно быть != null", paramName));
        }
        return number;
    }

    public Long checkLong(String value, String paramName) throws BadRequestException {
        Long number;
        try {
            number = Long.parseLong(value);
        }
        catch (Exception e) {
            throw new BadRequestException(String.format("%s должно быть числом", paramName));
        }
        return number;
    }

    public boolean checkBoolean(String value) {
        return Boolean.parseBoolean(value);
    }

    public VehicleType checkType(String value, String paramName) throws BadRequestException {
        try {
            return VehicleType.valueOf(value);
        }
        catch (Exception e) {
            throw new BadRequestException(String.format("%s должно быть одно из"+ Arrays.toString(
                VehicleType.values()), paramName));
        }
    }

    public FuelType checkFuelType(String value, String paramName) throws BadRequestException {
        try {
            return FuelType.valueOf(value);
        }
        catch (Exception e) {
            throw new BadRequestException(String.format("%s должно быть одно из"+ Arrays.toString(
                FuelType.values()), paramName));
        }
    }

}
