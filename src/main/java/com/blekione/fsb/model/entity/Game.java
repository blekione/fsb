package com.blekione.fsb.model.entity;

import com.blekione.fsb.model.GameStatus;

import java.time.LocalDateTime;

public record Game(String name, LocalDateTime createdOn, GameStatus status) {
    public static Game create(String name, LocalDateTime createdOn, GameStatus status) {
        return new Game(name, createdOn, status);
    }
}
