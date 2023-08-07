package com.blekione.fsb.model.dto;

import com.blekione.fsb.model.GameStatus;
import com.blekione.fsb.model.entity.Game;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.Objects;

public class GameDto {

    private String name;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime createdOn;
    private GameStatus status;

    /**
     * Required by JSON parser
     */
    GameDto() {
    }

    private GameDto(String name, LocalDateTime createdOn, GameStatus status) {
        this.name = name;
        this.createdOn = createdOn;
        this.status = status;
    }

    public static GameDto from(Game game) {
        return create(game.name(), game.createdOn(), game.status());
    }

    public static GameDto create(String name, LocalDateTime createdOn, GameStatus status) {
        return new GameDto(name, createdOn, status);
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public GameStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameDto gameDto = (GameDto) o;
        return Objects.equals(name, gameDto.name) && Objects.equals(createdOn, gameDto.createdOn) && status == gameDto.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, createdOn, status);
    }

    @Override
    public String toString() {
        return "GameDto{" +
                "name='" + name + '\'' +
                ", createdOn=" + createdOn +
                ", status=" + status +
                '}';
    }
}
