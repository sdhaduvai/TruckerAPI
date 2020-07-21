package io.egen.service;

import io.egen.entity.Reading;
import io.egen.entity.Vehicle;

import java.util.ArrayList;
import java.util.List;

public interface VehicleService {
    List<Vehicle> findAll();

    List<Vehicle> process(List<Vehicle> items);

    Vehicle findOne(String vin);

    Vehicle create(Vehicle veh);

    Vehicle update(String vin, Vehicle veh);

    List<List> getLocations(String vin);

    void delete(String vin);
}