package org.example.codiblybackend.dto;

import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDate;

@Builder
public record DailyWeatherDto(
    LocalDate date,
    Integer weatherCode,
    ExtremeTempsDto extremeTemperatures,
    Float generatedEnergy
) implements Serializable {
}
