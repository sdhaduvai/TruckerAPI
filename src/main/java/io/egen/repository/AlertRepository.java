package io.egen.repository;

import io.egen.entity.Alert;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AlertRepository extends CrudRepository<Alert, String> {
    List<Alert> findByVin(String vin);
    List<Alert> findByDateBetween(Date start, Date stop);
}