package sn.malcolm.demo.core.exception;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ConstraintViolationError extends ApiSubError {

    private final String field;
    private final String rejectedValue;
    private final List<String> messages;

    public ConstraintViolationError(String field, String rejectedValue, List<String> messages) {
        super(String.join(", ", messages));
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.messages = messages;
    }
}