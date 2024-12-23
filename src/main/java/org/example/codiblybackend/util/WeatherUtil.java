package org.example.codiblybackend.util;

import org.example.codiblybackend.client.Forecast;
import org.example.codiblybackend.dto.ExtremeTempsDto;
import org.example.codiblybackend.dto.PrecipitationSummary;

import java.util.List;

public class WeatherUtil {
    private static float calcAverage(List<? extends Number> values) {
        float sum = values.stream().reduce(
                0.f,
                (a, b) -> a + b.floatValue(),
                Float::sum
        );
        return sum / values.size();
    }

    public static float calcAverageSurfacePressure(Forecast forecast) {
        return calcAverage(forecast.hourly().surfacePressure());
    }

    public static float calcAverageSunshine(Forecast forecast) {
        return calcAverage(forecast.daily().sunshineDuration());
    }

    public static ExtremeTempsDto getExtremeTemps(Forecast forecast) {
        List<Float> tempsMax = forecast.daily().temperature2mMax();
        List<Float> tempsMin = forecast.daily().temperature2mMin();

        tempsMax.sort(Float::compareTo);
        tempsMin.sort(Float::compareTo);

        return ExtremeTempsDto.builder()
                .max(tempsMax.getLast())
                .min(tempsMin.getFirst())
                .build();
    }

    public static PrecipitationSummary getPrecipitationSummary(Forecast forecast) {
        List<Float> sortedPrecipitationSum = forecast.daily().precipitationSum().stream().sorted(Float::compareTo).toList();
        boolean isPrecipitation = sortedPrecipitationSum.get(3) > 0;
        return isPrecipitation ? PrecipitationSummary.PRECIPITATION : PrecipitationSummary.NO_PRECIPITATION;
    }
}
