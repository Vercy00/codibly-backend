package org.example.codiblybackend.controller.v1.request;

import io.swagger.v3.oas.annotations.Parameter;
import org.hibernate.validator.constraints.Range;

public record GetWeatherReq(
        @Parameter(description = "Latitude must be between -90 and 90 degrees", required = true)
        @Range(min = -90, max = 90, message = "Latitude must be between -90 and 90 degrees")
        float latitude,
        @Parameter(description = "Longitude must be between -180 and 180 degrees", required = true)
        @Range(min = -180, max = 180, message = "Longitude must be between -180 and 180 degrees")
        float longitude
) {
}
