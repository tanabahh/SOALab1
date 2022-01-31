package service;

import dao.VehicleDao;
import exception.BadRequestException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import model.Coordinates;
import model.FuelType;
import model.GroupByCreationDateDto;
import model.QueryAdditions;
import model.TableQueryAdditions;
import model.Vehicle;
import model.VehicleType;
import utils.Validation;

import javax.ejb.Stateless;

@Stateless
public class VehicleService implements VehicleServiceI {

    private final VehicleDao vehicleDao = new VehicleDao();
    private final Validation validation = new Validation();

    public List<Vehicle> getVehicle(
        Integer page,
        Integer perPage,
        String[] sortList,
        Map<String, String> filterMap
    ) throws BadRequestException {
        List<QueryAdditions> sortQueryAddition = new ArrayList<>();
        List<QueryAdditions> filterQueryAddition = new ArrayList<>();

        if (sortList != null) {
            try {
                Arrays.stream(sortList).map(
                    value -> {
                        boolean isAsc = validation.checkBoolean(value.split("&")[1]);
                        String tableAndColumn = value.split("&")[0];
                        if (!tableAndColumn.contains(".")) {
                            sortQueryAddition.add(
                                new QueryAdditions(TableQueryAdditions.VEHICLE, tableAndColumn,
                                    isAsc));
                        } else {
                            String table = tableAndColumn.split("\\.")[0];
                            String column = tableAndColumn.split("\\.")[1];
                            sortQueryAddition.add(
                                new QueryAdditions(TableQueryAdditions.valueOf(table), column,
                                    isAsc));
                        }
                        return null;
                    }
                ).collect(Collectors.toList());
            } catch (Exception ex) {
                throw new BadRequestException("Был неправильно задан параметр сортировки");
            }
        }

        if (!filterMap.isEmpty()) {
            filterMap.entrySet().forEach(
                x -> {
                    if (!x.getValue().isEmpty()) {
                        if (Objects.equals(x.getKey(), "x") || Objects.equals(x.getKey(), "y")) {
                            filterQueryAddition.add(
                                new QueryAdditions(TableQueryAdditions.COORDINATES, x.getKey(),
                                    x.getValue())
                            );
                        } else {
                            String column = null;
                            if (x.getKey().contains("-")) {
                                switch (x.getKey()) {
                                    case  "creation-date": column = "creationDate"; break;
                                    case "engine-power": column = "enginePower"; break;
                                    case "fuel-type": column = "fuelType"; break;
                                }
                            } else {
                                column = x.getKey();
                            }
                            filterQueryAddition.add(
                                new QueryAdditions(TableQueryAdditions.VEHICLE, column,
                                    x.getValue())
                            );
                        }
                    }
                }
            );
        }

        return vehicleDao.findAll(page, perPage, sortQueryAddition, filterQueryAddition);
    }

    public void save(Vehicle vehicle) {
        vehicleDao.save(vehicle);
    }

    public Vehicle getById(Integer id) {
        return vehicleDao.findById(id);
    }

    public void update(Integer id, String name, Integer x, Integer y,
    VehicleType type, FuelType fuelType, Long enginePower) throws BadRequestException{
        Vehicle vehicle = vehicleDao.findById(id);
        if (vehicle == null) throw new BadRequestException("not found vehicle");
        vehicle.setName(name);
        vehicle.setCoordinates(new Coordinates(x, y));
        vehicle.setType(type);
        vehicle.setFuelType(fuelType);
        vehicle.setEnginePower(enginePower);
        vehicleDao.update(vehicle);
    }

    public void delete(Integer id) throws BadRequestException {
        Vehicle vehicle = vehicleDao.findById(id);
        if (vehicle == null) throw new BadRequestException("not found vehicle");
        vehicleDao.delete(vehicle);
    }

    public void deleteWithEnginePower(Long enginePower) {
        vehicleDao.deleteWhereEnginePower(enginePower);
    }

    public Vehicle getMaxCreatingDate() {
        return vehicleDao.getWithMaxCreatingDate();
    }

    public List<GroupByCreationDateDto> groupByCreationDate() {
        return vehicleDao.groupByCreationDate();
    }
}
