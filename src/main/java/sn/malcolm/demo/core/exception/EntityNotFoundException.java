package sn.malcolm.demo.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class<?> clazz, String field, String value) {
        super(String.format("%s not found with %s: %s", clazz.getSimpleName(), field, value));
    }
}
