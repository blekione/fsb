package com.blekione.fsb.model;

public enum GameStatus {
    INACTIVE, ACTIVE, UNRECOGNIZED;

    public static GameStatus from(String newStatus) {
        for (GameStatus value : GameStatus.values()) {
            if (value.toString().equals(newStatus)) {
                return value;
            }
        }
        return UNRECOGNIZED;
    }
}
