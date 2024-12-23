package org.example.codiblybackend.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record Forecast(
        float latitude,
        float longitude,
        @JsonProperty("generationtime_ms")
        float generationtimeMs,
        int utc_offset_seconds,
        String timezone,
        @JsonProperty("timezone_abbreviation")
        String timezoneAbbreviation,
        int elevation,
        @JsonProperty("hourly_units")
        HourlyUnits hourlyUnits,
        Hourly hourly,
        @JsonProperty("daily_units")
        DailyUnits dailyUnits,
        Daily daily
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record HourlyUnits(
            String time,
            @JsonProperty("surface_pressure")
            String surfacePressure
    ) {
    }

    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Hourly(
            List<LocalDateTime> time,
            @JsonProperty("surface_pressure")
            List<Float> surfacePressure
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DailyUnits(
            String time,
            @JsonProperty("weather_code")
            String weatherCode,
            @JsonProperty("temperature_2m_max")
            String temperature2mMax,
            @JsonProperty("temperature_2m_min")
            String temperature2mMin,
            @JsonProperty("sunshine_duration")
            String sunshineDuration,
            @JsonProperty("precipitation_sum")
            String precipitationSum
    ) {
    }

    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Daily(
            List<LocalDate> time,
            @JsonProperty("weather_code")
            List<Integer> weatherCode,
            @JsonProperty("temperature_2m_max")
            List<Float> temperature2mMax,
            @JsonProperty("temperature_2m_min")
            List<Float> temperature2mMin,
            @JsonProperty("sunshine_duration")
            List<Float> sunshineDuration,
            @JsonProperty("precipitation_sum")
            List<Float> precipitationSum
    ) {
    }
}
