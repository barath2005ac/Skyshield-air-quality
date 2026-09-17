package com.skyshield.api;
import jakarta.validation.constraints.*;
import java.time.Instant;
public record ReadingRequest(@NotBlank String nodeCode, Instant recordedAt, @PositiveOrZero double pm25, @PositiveOrZero double pm10, @PositiveOrZero double gasPpm, @DecimalMin("0") @DecimalMax("100") double oxygenPercent, @PositiveOrZero double co2Ppm, @PositiveOrZero double coPpm, @PositiveOrZero double no2Ppm, double temperature, @DecimalMin("0") @DecimalMax("100") double humidity) {}
