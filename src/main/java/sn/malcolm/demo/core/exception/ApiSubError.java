package sn.malcolm.demo.core.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApiSubError {
    private String message;
    private HttpStatus status;

    public ApiSubError(String message) {
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
    }
}
