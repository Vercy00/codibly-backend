package org.example.codiblybackend.service;

import org.example.codiblybackend.dto.DailyWeatherDto;
import org.example.codiblybackend.controller.v1.request.GetWeatherReq;
import org.example.codiblybackend.dto.SummaryRes;

import java.util.List;

public interface WeatherService {
    List<DailyWeatherDto> getDailyWeather(GetWeatherReq req);
    SummaryRes getSummary(GetWeatherReq req);
}
