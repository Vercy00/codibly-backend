package org.example.codiblybackend.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.codiblybackend.dto.ErrorMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandler {
    private final ObjectMapper objectMapper;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageDto> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = Objects.requireNonNull(ex.getFieldError()).getDefaultMessage();
        log.error(errorMessage);
        return ResponseEntity.badRequest().body(new ErrorMessageDto(true, errorMessage));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(FeignException.BadRequest.class)
    public ResponseEntity<ErrorMessageDto> handleFeignBadRequestException(FeignException.BadRequest ex) throws JsonProcessingException {
        String errorReason = "Bad request";

        if (ex.request().url().contains("api.open-meteo.com")) {
            var type = new TypeReference<HashMap<String, String>>() {};
            Map<String, String> obj = objectMapper.readValue(ex.contentUTF8(), type);
            errorReason = obj.get("reason");
        }

        String errorMessage = "Feign error: " + errorReason;
        log.error(errorMessage);
        return ResponseEntity.badRequest().body(new ErrorMessageDto(true, errorMessage));
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @org.springframework.web.bind.annotation.ExceptionHandler(FeignException.ServiceUnavailable.class)
    public ResponseEntity<ErrorMessageDto> handleFeignServiceUnavailableException(FeignException.ServiceUnavailable ex) {
        String errorMessage = "Feign error: Service unavailable";

        if (ex.request().url().contains("api.open-meteo.com"))
            errorMessage = "Feign error: Open Meteo is unavailable";

        log.error(errorMessage);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorMessageDto(true, errorMessage));
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @org.springframework.web.bind.annotation.ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorMessageDto> handleFeignException(FeignException ex) {
        String errorMessage = ("Feign error: An unknown error occurred with code: %d" +
                " while requesting the URL: %s").formatted(ex.status(), ex.request().url());

        log.error(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ErrorMessageDto(true, errorMessage));
    }
}
