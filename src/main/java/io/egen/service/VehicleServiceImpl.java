package io.egen.service;

import io.egen.entity.Reading;
import io.egen.entity.Vehicle;
import io.egen.exception.BadRequestException;
import io.egen.exception.ResourceNotFoundException;
import io.egen.repository.ReadingRepository;
import io.egen.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    VehicleRepository repository;

    @Autowired
    ReadingRepository readingRepository;

    @Transactional(readOnly = true)
    public List<Vehicle> findAll() {
        return (List<Vehicle>) repository.findAll();
    }

    @Transactional(readOnly = true)
    public Vehicle findOne(String vin) {
        return repository.findById(vin)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Vehicle with id " + vin + " doesn't exist."));
    }

    @Transactional
    public Vehicle create(Vehicle veh) {
        Optional<Vehicle> existing = repository.findById(veh.getVin());
        if (existing.isPresent()) {
            throw new BadRequestException("Vehicle with id " + veh.getVin() + " already exists.");
        }
        return repository.save(veh);
    }

    @Transactional
    public List<Vehicle> process(List<Vehicle> items) {
        for (Vehicle item: items){
            Optional<Vehicle> existing = repository.findById(item.getVin());
            if (existing.isPresent()){
                update(item.getVin(), item);
            }
            else{
                create(item);
            }
        }
        return items;
    }

    @Transactional
    public Vehicle update(String vin, Vehicle veh) {
        Optional<Vehicle> existing = repository.findById(vin);
        if (!existing.isPresent()) {
            throw new ResourceNotFoundException("Employee with id " + vin + " doesn't exist.");
        }
        return repository.save(veh);
    }

    @Transactional
    public void delete(String vin) {
        Optional<Vehicle> existing = repository.findById(vin);
        if (!existing.isPresent()) {
            throw new ResourceNotFoundException("Employee with id " + vin + " doesn't exist.");
        }
        repository.delete(existing.get());
    }

    @Transactional
    public List<List> getLocations(String vin) {
        List<Reading> readings = readingRepository.findReadings(vin);

        List<List> locations = new ArrayList<List>();

        for(Reading reading : readings){
            locations.add(Arrays.asList(reading.getLatitude(), reading.getLongitude()));
        }
        return locations;
    }
}