package sn.malcolm.demo.core.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private HttpStatus status = HttpStatus.BAD_REQUEST;

    public ApiException() {
        super();
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.status = httpStatus;
    }

    public ApiException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.status = httpStatus;
    }

    @Override
    public String toString() {
        return String.format("ApiException{message='%s', status=%s, cause=%s}",
                getMessage(),
                status,
                getCause() != null ? getCause().toString() : "null");
    }


}
