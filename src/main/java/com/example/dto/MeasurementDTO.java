package com.example.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import com.example.models.Sensor;

public class MeasurementDTO {

    @NotNull(message = "Value must not be empty")
    @Min(value = -100, message = "Value must be between -100 and 100")
    @Max(value = 100, message = "Value must be between -100 and 100")
    @Column(name = "value")
    private float value;

    @NotNull(message = "Raining must not be empty")
    @Column(name = "raining")
    private boolean raining;

    @ManyToOne
    @JoinColumn(name = "sensor_id", referencedColumnName = "name")
    private Sensor sensor;

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public boolean isRaining() {
        return raining;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
}
