package com.example.controllers;

import com.example.dto.SensorDTO;
import com.example.models.Sensor;
import com.example.services.SensorService;
import com.example.util.*;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.example.util.ErrorsUtil.notCreatedException;

import java.util.List;

@RestController
@RequestMapping("/sensor")
public class SensorController {

    private final SensorService sensorService;
    private final SensorValidator sensorValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public SensorController(SensorService sensorService, SensorValidator sensorValidator, ModelMapper modelMapper) {
        this.sensorService = sensorService;
        this.sensorValidator = sensorValidator;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid SensorDTO sensor, BindingResult bindingResult) {

        Sensor newSensor = convertToSensor(sensor);
        sensorValidator.validate(newSensor, bindingResult);

        if (bindingResult.hasErrors())
            notCreatedException(bindingResult);

        sensorService.saveNewSensor(newSensor);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping()
    public List<Sensor> getAllSensors() {
        return sensorService.findAllSensors();
    }

    @GetMapping("/{id}")
    public Sensor getSensorById(@PathVariable("id") int id) {
        return sensorService.findSensorById(id);
    }

    public Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorNotFoundException e) {
        SensorErrorResponse response = new SensorErrorResponse(
                "The sensor by this name was not found",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(NotCreatedException e) {
        SensorErrorResponse response = new SensorErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
