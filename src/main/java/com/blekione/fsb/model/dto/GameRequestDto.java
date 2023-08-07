package com.blekione.fsb.model.dto;

import java.util.Objects;

public class GameRequestDto {
    private String name;

    /**
     * Required by JSON parser
     */
    GameRequestDto() {
    }

    public GameRequestDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameRequestDto that = (GameRequestDto) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "GameRequestDto{" +
                "name='" + name + '\'' +
                '}';
    }
}
