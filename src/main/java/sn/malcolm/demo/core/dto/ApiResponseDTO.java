package sn.malcolm.demo.core.dto;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import sn.malcolm.demo.view.View;

@Setter
@Getter
public class ApiResponseDTO {
    @JsonView({View.Public.class})
    private Boolean success;
    @JsonView({View.Public.class})
    private String message;
    @JsonView({View.Public.class})
    private Object data;

    public ApiResponseDTO(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResponseDTO(Boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}

