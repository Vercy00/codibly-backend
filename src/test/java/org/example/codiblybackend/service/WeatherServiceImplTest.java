package org.example.codiblybackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.codiblybackend.client.Forecast;
import org.example.codiblybackend.client.OpenMeteoClient;
import org.example.codiblybackend.controller.v1.request.GetWeatherReq;
import org.example.codiblybackend.dto.DailyWeatherDto;
import org.example.codiblybackend.dto.ExtremeTempsDto;
import org.example.codiblybackend.dto.SummaryRes;
import org.example.codiblybackend.util.PhotovoltaicsUtil;
import org.example.codiblybackend.util.WeatherUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceImplTest {
    @Mock
    private OpenMeteoClient openMeteoClient;

    @InjectMocks
    private WeatherServiceImpl underTest;

    @Test
    void getDailyWeather() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
                .findAndRegisterModules();
        String json = new String(
                Objects.requireNonNull(getClass().getResourceAsStream("/tests/daily-weather-test-object.json")).readAllBytes(),
                StandardCharsets.UTF_8
        );
        Forecast forecast = objectMapper.readValue(json, Forecast.class);
        GetWeatherReq req = new GetWeatherReq(52.52f, 13.419998f);
        var daily = forecast.daily();
        DailyWeatherDto expectedValue = DailyWeatherDto.builder()
                .date(daily.time().getFirst())
                .weatherCode(daily.weatherCode().getFirst())
                .extremeTemperatures(ExtremeTempsDto.builder()
                        .min(daily.temperature2mMin().getFirst())
                        .max(daily.temperature2mMax().getFirst())
                        .build())
                .generatedEnergy(PhotovoltaicsUtil.calcGeneratedEnergy(daily.sunshineDuration().getFirst()))
                .build();

        when(openMeteoClient.getForecast(any(Float.class), any(Float.class), any(), any(), any(String.class))).thenReturn(forecast);

        List<DailyWeatherDto> result = underTest.getDailyWeather(req);
        assertThat(result.size()).isEqualTo(7);
        assertThat(result.getFirst()).isEqualTo(expectedValue);
    }

    @Test
    void getSummary() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
                .findAndRegisterModules();
        String json = new String(
                Objects.requireNonNull(getClass().getResourceAsStream("/tests/summary-test-object.json")).readAllBytes(),
                StandardCharsets.UTF_8
        );
        Forecast forecast = objectMapper.readValue(json, Forecast.class);
        GetWeatherReq req = new GetWeatherReq(52.52f, 13.419998f);
        SummaryRes expectedValue = SummaryRes.builder()
                .averageSurfacePressure(WeatherUtil.calcAverageSurfacePressure(forecast))
                .averageSunshine(WeatherUtil.calcAverageSunshine(forecast))
                .extremeTemperatures(WeatherUtil.getExtremeTemps(forecast))
                .precipitationSummary(WeatherUtil.getPrecipitationSummary(forecast))
                .build();

        when(openMeteoClient.getForecast(any(Float.class), any(Float.class), any(), any(), any(String.class))).thenReturn(forecast);

        assertThat(underTest.getSummary(req)).isEqualTo(expectedValue);
    }
}