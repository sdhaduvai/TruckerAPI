package io.egen.service;

import io.egen.entity.Reading;
import io.egen.exception.BadRequestException;
import io.egen.exception.ResourceNotFoundException;
import io.egen.repository.ReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReadingServiceImpl implements ReadingService {

    @Autowired
    ReadingRepository repository;

    @Transactional(readOnly = true)
    public List<Reading> findAll() {
        return (List<Reading>) repository.findAll();
    }

    @Transactional(readOnly = true)
    public Reading findOne(String vin) {
        return repository.findById(vin)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Vehicle with id " + vin + " doesn't exist."));
    }

    @Transactional
    public Reading create(Reading read) {
        return repository.save(read);
    }

    @Transactional
    public Reading update(String vin, Reading read) {
        Optional<Reading> existing = repository.findById(vin);
        if (!existing.isPresent()) {
            throw new ResourceNotFoundException("Employee with id " + vin + " doesn't exist.");
        }
        return repository.save(read);
    }

    @Transactional
    public void delete(String vin) {
        Optional<Reading> existing = repository.findById(vin);
        if (!existing.isPresent()) {
            throw new ResourceNotFoundException("Employee with id " + vin + " doesn't exist.");
        }
        repository.delete(existing.get());
    }
}