package io.egen.service;

import io.egen.entity.Reading;

import java.util.List;

public interface ReadingService {
    List<Reading> findAll();

    Reading findOne(String vin);

    Reading create(Reading read);

    Reading update(String vin, Reading read);

    void delete(String vin);
}