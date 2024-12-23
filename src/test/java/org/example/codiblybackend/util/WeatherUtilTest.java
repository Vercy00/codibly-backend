package org.example.codiblybackend.util;

import org.example.codiblybackend.client.Forecast;
import org.example.codiblybackend.dto.ExtremeTempsDto;
import org.example.codiblybackend.dto.PrecipitationSummary;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class WeatherUtilTest {
    @Test
    void calcAverageSurfacePressure() {
        var hourly = Forecast.Hourly.builder()
                .surfacePressure(List.of(1f, 2f, 3f))
                .build();
        var forecast = Forecast.builder()
                .hourly(hourly)
                .build();
        float average = (1f + 2f + 3f) / 3;

        assertThat(WeatherUtil.calcAverageSurfacePressure(forecast)).isEqualTo(average);
    }

    @Test
    void getPrecipitationSummary() {
        Forecast forecast = Forecast.builder()
                .daily(Forecast.Daily.builder()
                        .precipitationSum(List.of(0f, 0f, 0f, 0f, 0.5f, 1.2f, 1.5f))
                        .build())
                .build();

        assertThat(WeatherUtil.getPrecipitationSummary(forecast)).isEqualTo(PrecipitationSummary.NO_PRECIPITATION);

        forecast = Forecast.builder()
                .daily(Forecast.Daily.builder()
                        .precipitationSum(List.of(0f, 0f, 0.3f, 0.2f, 0.5f, 1.2f, 1.5f))
                        .build())
                .build();

        assertThat(WeatherUtil.getPrecipitationSummary(forecast)).isEqualTo(PrecipitationSummary.PRECIPITATION);
    }

    @Test
    void getExtremeTemps() {
        Forecast forecast = Forecast.builder()
                .daily(Forecast.Daily.builder()
                        .temperature2mMin(new ArrayList<>(List.of(-2.3f, -1.3f, 0.3f, -0.1f, 0.5f, 1.2f, -5.5f)))
                        .temperature2mMax(new ArrayList<>(List.of(-2.3f, -1.3f, 3.3f, -0.1f, 0.5f, 1.2f, -5.5f)))
                        .build())
                .build();
        var expected = ExtremeTempsDto.builder()
                .min(-5.5f)
                .max(3.3f)
                .build();

        assertThat(WeatherUtil.getExtremeTemps(forecast)).isEqualTo(expected);
    }
}