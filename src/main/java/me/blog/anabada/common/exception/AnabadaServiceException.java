package me.blog.anabada.common.exception;

import lombok.Getter;

@Getter
public class AnabadaServiceException extends RuntimeException {
    private final String message;

    public AnabadaServiceException(String errorMessage) {
        super(errorMessage);
        this.message = errorMessage;
    }

    public AnabadaServiceException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.message = errorMessage;
    }
}
