package io.egen.service;

import io.egen.entity.Alert;
import io.egen.entity.Reading;

import java.util.Date;
import java.util.List;

public interface AlertService {
    List<Alert> findAll();

    Alert findOne(String id);

    void create(Reading read);

    List<Alert> vehicleAlert(String vin);

    Alert update(String id, Alert ale);

    List<Alert> findLastNHours(int interval);

    void delete(String id);
}