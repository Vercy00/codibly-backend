package org.example.codiblybackend.dto;

public record ErrorMessageDto(
        boolean error,
        String message
) {
}
