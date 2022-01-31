package service;

import exception.BadRequestException;
import model.FuelType;
import model.GroupByCreationDateDto;
import model.Vehicle;
import model.VehicleType;

import javax.ejb.Remote;
import java.util.List;
import java.util.Map;

@Remote
public interface VehicleServiceI {
    List<Vehicle> getVehicle(Integer page, Integer perPage, String[] sortList, Map<String, String> filterMap) throws BadRequestException;
    void save(Vehicle vehicle);
    Vehicle getById(Integer id);
    void update(Integer id, String name, Integer x, Integer y, VehicleType type, FuelType fuelType, Long enginePower) throws BadRequestException;
    void delete(Integer id) throws BadRequestException;
    void deleteWithEnginePower(Long enginePower);
    Vehicle getMaxCreatingDate();
    List<GroupByCreationDateDto> groupByCreationDate();
}
