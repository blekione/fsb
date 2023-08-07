package com.blekione.fsb.exception;

public class GameException extends ApplicationException {

    public GameException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameException(String message) {
        super(message);
    }
}
