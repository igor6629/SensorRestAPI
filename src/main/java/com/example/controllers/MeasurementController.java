package com.example.controllers;

import com.example.dto.MeasurementDTO;
import com.example.models.Measurement;
import com.example.services.MeasurementService;
import com.example.util.*;
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
@RequestMapping("/measurement")
public class MeasurementController {

    private final MeasurementService measurementService;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementController(MeasurementService measurementService, ModelMapper modelMapper) {
        this.measurementService = measurementService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<Measurement> getAllMeasurements() {
        return measurementService.findAllMeasurements();
    }

    @GetMapping("/{id}")
    public Measurement getMeasurementById(@PathVariable("id") int id) {
        Measurement measurement = measurementService.findMeasurementById(id);

        System.out.println(measurement.getSensor().getName());

        return measurement;
    }

    @GetMapping("/rainyDaysCount")
    public int getRainyDaysCount() {
        return measurementService.findOfRainyDays();
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addNewMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder message = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();

            for (FieldError error : errors)
                message.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(";");

            throw new MeasurementNotCreatedException(message.toString());
        }

        measurementService.createNewMeasurement(convertToMeasurement(measurementDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    public Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(MeasurementNotFoundException e) {
        SensorErrorResponse response = new SensorErrorResponse(
                "Measurement with this id did not find!",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(MeasurementNotCreatedException e) {
        SensorErrorResponse response = new SensorErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
