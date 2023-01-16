package mapoints.lib.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private boolean status;

    @Getter
    @Setter
    private int statusCode;

    @Getter
    @Setter
    private Date time = new Date();

    public ApiResponse() {}

    public ApiResponse(boolean status, int statusCode) {
        this.status = status;
        this.statusCode = statusCode;
    }

    public ApiResponse(boolean status, int statusCode, String message) {
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
    }
}
