package io.egen.repository;

import io.egen.entity.Alert;
import io.egen.entity.Reading;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ReadingRepository extends CrudRepository<Reading, String> {
    List<Reading> findByVin(String vin);

    @Query(value = "SELECT * FROM reading WHERE vin = :vin and timestamp >= DATE_SUB(NOW(),INTERVAL 0.5 HOUR) ORDER BY timestamp ASC", nativeQuery = true)
    List<Reading> findReadings(@Param("vin") String vin);
}