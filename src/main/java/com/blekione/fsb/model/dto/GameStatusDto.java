package com.blekione.fsb.model.dto;

import java.util.Objects;

public class GameStatusDto {
    private String newStatus;

    /**
     * Required by JSON parser
     */
    GameStatusDto() {
    }

    public GameStatusDto(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameStatusDto that = (GameStatusDto) o;
        return Objects.equals(newStatus, that.newStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newStatus);
    }

    @Override
    public String toString() {
        return "GameStatusDto{" +
                "newStatus='" + newStatus + '\'' +
                '}';
    }
}
