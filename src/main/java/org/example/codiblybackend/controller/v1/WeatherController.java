package org.example.codiblybackend.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import org.example.codiblybackend.controller.v1.request.GetWeatherReq;
import org.example.codiblybackend.dto.DailyWeatherDto;
import org.example.codiblybackend.dto.SummaryRes;
import org.example.codiblybackend.service.WeatherService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/weather")
public record WeatherController(
        WeatherService weatherService
) {
    @GetMapping
    @Operation(summary = "Get a forecast for next 7 days (includes today)")
    public ResponseEntity<List<DailyWeatherDto>> getWeather(
            @Validated
            @ParameterObject
            GetWeatherReq req
    ) {
        return ResponseEntity.ok(weatherService.getDailyWeather(req));
    }

    @GetMapping("/summary")
    @Operation(summary = "Get a summary for next 7 days (includes today)")
    public ResponseEntity<SummaryRes> getSummary(
            @Validated
            @ParameterObject
            GetWeatherReq req
    ) {
        return ResponseEntity.ok(weatherService.getSummary(req));
    }
}
