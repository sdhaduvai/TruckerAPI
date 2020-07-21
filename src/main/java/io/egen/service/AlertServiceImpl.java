package io.egen.service;

import io.egen.entity.Alert;
import io.egen.entity.Reading;
import io.egen.entity.Tires;
import io.egen.entity.Vehicle;
import io.egen.exception.BadRequestException;
import io.egen.exception.ResourceNotFoundException;
import io.egen.repository.AlertRepository;
import io.egen.repository.VehicleRepository;
import io.egen.repository.ReadingRepository;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

@Service
public class AlertServiceImpl implements AlertService{

    @Autowired
    AlertRepository repository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    ReadingRepository readingRepository;

    Logger log = LoggerFactory.getLogger(AlertServiceImpl.class);

    @Transactional(readOnly = true)
    public List<Alert> findAll() {
        return (List<Alert>) repository.findAll();
    }

    @Transactional(readOnly = true)
    public Alert findOne(String id) {
        return repository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Employee with id " + id + " doesn't exist."));
    }

    @Transactional
    public void create(Reading reading) {
        Optional<Vehicle> existing = vehicleRepository.findById(reading.getVin());

        if (!existing.isPresent()){
           throw new ResourceNotFoundException("Vehicle with id " + reading.getVin() + " doesn't exist.");
        }

        Vehicle vehicle = existing.get();

        if (reading.getEngineRpm() > vehicle.getRedlineRpm()){
            String message = "Engine pressure is high";
            Alert alert = new Alert();
            alert.setVin(reading.getVin());
            alert.setPriority("HIGH");
            alert.setMessage(message);
            alert.setDate(reading.getTimestamp());
            repository.save(alert);
        }

        if (reading.getFuelVolume() < 0.1 * vehicle.getMaxFuelVolume()){
            String message = "Fuel is low";
            Alert alert = new Alert();
            alert.setVin(reading.getVin());
            alert.setPriority("MEDIUM");
            alert.setMessage(message);
            alert.setDate(reading.getTimestamp());
            repository.save(alert);

        }

        Tires tires = reading.getTires();
        if (isTyreProblem(tires)){
            String message = "Low Tyre Pressure";
            Alert alert = new Alert();
            alert.setVin(reading.getVin());
            alert.setPriority("LOW");
            alert.setMessage(message);
            alert.setDate(reading.getTimestamp());
            repository.save(alert);
        }

    }

    private boolean isTyreProblem(Tires tires) {
        if (tires.getFrontRight() < 32 || tires.getFrontLeft() < 32 || tires.getRearLeft() < 32 || tires.getRearRight() < 32 || tires.getFrontRight() > 36 || tires.getFrontLeft() > 36 || tires.getRearLeft() > 36 || tires.getRearRight() > 36){
            return true;
        }
        return false;
    }

    @Transactional
    public Alert update(String id, Alert ale) {
        Optional<Alert> existing = repository.findById(id);
        if (!existing.isPresent()) {
            throw new ResourceNotFoundException("Alert with id" + id + " doesn't exist.");
        }
        return repository.save(ale);
    }

    @Transactional
    public void delete(String id) {
        Optional<Alert> existing = repository.findById(id);
        if (!existing.isPresent()) {
            throw new ResourceNotFoundException("Employee with id " + id + " doesn't exist.");
        }
        repository.delete(existing.get());
    }

    @Transactional
    public List<Alert> vehicleAlert(String vin) {
        return repository.findByVin(vin);
    }

    @Transactional
    public List<Alert> findLastNHours(int interval) {
        Date stop = new Date();

        long currentDateTime = System.currentTimeMillis();
        Date start = new Date(currentDateTime - interval*60*60*1000);

        List<Alert> result =  repository.findByDateBetween(start, stop);


        result.sort((a, b) -> {
            return a.getVin().compareTo(b.getVin());
        });
        return result;
    }
}