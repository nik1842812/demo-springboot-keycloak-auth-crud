package sn.malcolm.demo.core.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class BadRequestException extends RuntimeException {

    private final HttpStatus status;
    private final List<ApiSubError> errors;

    public BadRequestException(String message, List<ApiSubError> errors) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.errors = errors;
    }

    public BadRequestException(String message, HttpStatus status, List<ApiSubError> errors) {
        super(message);
        this.status = status;
        this.errors = errors;
    }
}