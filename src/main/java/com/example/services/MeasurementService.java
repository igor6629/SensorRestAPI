package com.example.services;

import com.example.models.Measurement;
import com.example.models.Sensor;
import com.example.repositories.MeasurementRepository;
import com.example.util.MeasurementNotFoundException;
import com.example.util.NotCreatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final SensorService sensorService;

    @Autowired
    public MeasurementService(SensorService sensorService, MeasurementRepository measurementRepository) {
        this.sensorService = sensorService;
        this.measurementRepository = measurementRepository;
    }

    public List<Measurement> findAllMeasurements() {
        return measurementRepository.findAll();
    }

    public int findOfRainyDays() {
        return measurementRepository.countByRainingIsTrue();
    }

    public Measurement findMeasurementById(int id) {
        return measurementRepository.findById(id).orElseThrow(MeasurementNotFoundException::new);
    }

    @Transactional
    public void createNewMeasurement(Measurement measurement) {

        Sensor currentSensor = sensorService.getSensorByName(measurement.getSensor().getName()).orElse(null);

        if (currentSensor == null)
            throw new NotCreatedException("The sensor by this name was not found");

        measurement.setCreatedAt(LocalDateTime.now());
        measurement.setSensor(currentSensor);

        measurementRepository.save(measurement);
    }
}
