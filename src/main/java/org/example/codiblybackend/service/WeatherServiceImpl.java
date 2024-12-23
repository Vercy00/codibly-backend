package org.example.codiblybackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.codiblybackend.client.OpenMeteoClient;
import org.example.codiblybackend.client.Forecast;
import org.example.codiblybackend.dto.DailyWeatherDto;
import org.example.codiblybackend.controller.v1.request.GetWeatherReq;
import org.example.codiblybackend.dto.ExtremeTempsDto;
import org.example.codiblybackend.dto.SummaryRes;
import org.example.codiblybackend.util.PhotovoltaicsUtil;
import org.example.codiblybackend.util.WeatherUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    private final OpenMeteoClient openMeteoClient;

    @Override
    public List<DailyWeatherDto> getDailyWeather(GetWeatherReq req) {
        log.info("Getting weather for {}", req);
        Forecast forecast = openMeteoClient.getForecast(
                req.latitude(),
                req.longitude(),
                null,
                List.of(
                        "weather_code",
                        "temperature_2m_max",
                        "temperature_2m_min",
                        "sunshine_duration"
                ),
                "Europe/Warsaw"
        );
        List<DailyWeatherDto> dailyWeather = new ArrayList<>();
        Forecast.Daily daily = forecast.daily();

        for (int i = 0; i < forecast.daily().time().size(); i++) {
            var day = DailyWeatherDto.builder()
                    .date(daily.time().get(i))
                    .weatherCode(daily.weatherCode().get(i))
                    .extremeTemperatures(ExtremeTempsDto.builder()
                            .min(daily.temperature2mMin().get(i))
                            .max(daily.temperature2mMax().get(i))
                            .build())
                    .generatedEnergy(PhotovoltaicsUtil.calcGeneratedEnergy(daily.sunshineDuration().get(i)))
                    .build();

            dailyWeather.add(day);
        }

        return dailyWeather;
    }

    @Override
    public SummaryRes getSummary(GetWeatherReq req) {
        log.info("Getting summary for {}", req);
        Forecast forecast = openMeteoClient.getForecast(
                req.latitude(),
                req.longitude(),
                List.of("surface_pressure"),
                List.of(
                        "sunshine_duration",
                        "temperature_2m_max",
                        "temperature_2m_min",
                        "precipitation_sum"
                ),
                "Europe/Warsaw"
        );

        return SummaryRes.builder()
                .averageSurfacePressure(WeatherUtil.calcAverageSurfacePressure(forecast))
                .averageSunshine(WeatherUtil.calcAverageSunshine(forecast))
                .extremeTemperatures(WeatherUtil.getExtremeTemps(forecast))
                .precipitationSummary(WeatherUtil.getPrecipitationSummary(forecast))
                .build();
    }
}
