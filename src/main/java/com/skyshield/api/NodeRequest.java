package com.skyshield.api;
import jakarta.validation.constraints.NotBlank;
public record NodeRequest(@NotBlank String nodeCode, @NotBlank String name, @NotBlank String location) {}
