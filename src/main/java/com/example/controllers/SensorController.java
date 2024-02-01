package com.example.controllers;

import com.example.dto.SensorDTO;
import com.example.models.Sensor;
import com.example.services.SensorService;
import com.example.util.SensorErrorResponse;
import com.example.util.SensorNotCreatedException;
import com.example.util.SensorNotFoundException;
import com.example.util.SensorValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

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

        //TODO: Make a new function in util package to handle errors
        if (bindingResult.hasErrors()) {
            StringBuilder message = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();

            for (FieldError error : errors)
                message.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(";");

            throw new SensorNotCreatedException(message.toString());
        }

        sensorService.saveNewSensor(newSensor);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    public Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }


    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorNotFoundException e) {
        SensorErrorResponse response = new SensorErrorResponse(
                "Sensor with this id didn't found!",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorNotCreatedException e) {
        SensorErrorResponse response = new SensorErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
