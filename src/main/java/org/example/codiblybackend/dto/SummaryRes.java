package org.example.codiblybackend.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record SummaryRes(
        Float averageSurfacePressure,
        Float averageSunshine,
        ExtremeTempsDto extremeTemperatures,
        PrecipitationSummary precipitationSummary
) implements Serializable {
}
