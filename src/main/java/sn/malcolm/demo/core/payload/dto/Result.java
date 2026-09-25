package sn.malcolm.demo.core.payload.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import sn.malcolm.demo.view.View;

@Getter
@Setter
public class Result<T> {
    @JsonView({View.Public.class})
    private Boolean success;
    @JsonView({View.Public.class})
    private String message;
    @JsonView({View.Public.class})
    private T data;

    private Result(Boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success() {
        return new Result<>(true, null, null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(true, null, data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(true, message, data);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(false, message, null);
    }

    public static <T> Result<T> error(String message, T data) {
        return new Result<>(false, message, data);
    }
}