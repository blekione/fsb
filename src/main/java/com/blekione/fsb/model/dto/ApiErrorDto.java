package com.blekione.fsb.model.dto;

import com.blekione.fsb.configuration.ApplicationContextProvider;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.http.HttpStatus;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

public class ApiErrorDto {
    private HttpStatus httpStatus;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;

    /**
     * Required by JSON parser
     */
    ApiErrorDto() {
    }

    public ApiErrorDto(HttpStatus httpStatus, Throwable ex, LocalDateTime timestamp) {
        this.httpStatus = httpStatus;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
        this.timestamp = timestamp;
    }

    public static ApiErrorDto create(HttpStatus httpStatus, Throwable ex) {
        Clock clock = ApplicationContextProvider.getApplicationContext().getBean(Clock.class);
        return new ApiErrorDto(httpStatus, ex, LocalDateTime.now(clock));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiErrorDto that = (ApiErrorDto) o;
        return httpStatus == that.httpStatus && Objects.equals(timestamp, that.timestamp) && Objects.equals(message, that.message) && Objects.equals(debugMessage, that.debugMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpStatus, timestamp, message, debugMessage);
    }

    @Override
    public String toString() {
        return "ApiErrorDto{" +
                "httpStatus=" + httpStatus +
                ", timestamp=" + timestamp +
                ", message='" + message + '\'' +
                ", debugMessage='" + debugMessage + '\'' +
                '}';
    }
}
