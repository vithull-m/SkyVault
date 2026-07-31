package com.skyvault.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SkyVaultApiException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public SkyVaultApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }
}
