package com.example.services;

import com.example.models.Sensor;
import com.example.repositories.SensorRepository;
import com.example.util.SensorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SensorService {
    private final SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public List<Sensor> findAllSensors() {
        return sensorRepository.findAll();
    }

    public Sensor findSensorById(int id) {
        return sensorRepository.findById(id).orElseThrow(SensorNotFoundException::new);
    }

    public Optional<Sensor> getSensorByName(String name) {
        return sensorRepository.findSensorByName(name);
    }

    @Transactional
    public void saveNewSensor(Sensor sensor) {
        sensorRepository.save(sensor);
    }
}
