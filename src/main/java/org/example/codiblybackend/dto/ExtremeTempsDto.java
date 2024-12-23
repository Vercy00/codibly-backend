package org.example.codiblybackend.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record ExtremeTempsDto(
        Float min,
        Float max
) implements Serializable {
}
