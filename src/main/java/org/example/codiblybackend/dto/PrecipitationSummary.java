package org.example.codiblybackend.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PrecipitationSummary {
    PRECIPITATION, NO_PRECIPITATION;

    @JsonValue
    public String toLowerCase() {
        return name().toLowerCase();
    }
}
