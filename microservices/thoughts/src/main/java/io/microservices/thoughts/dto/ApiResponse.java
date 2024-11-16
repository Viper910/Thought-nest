package io.microservices.thoughts.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private List<String> errors;


    public ApiResponse(boolean success, T data, String message, List<String> errors) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.errors = errors;
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message, null);
    }

    public static <T> ApiResponse<T> error(String message, List<String> errors) {
        return new ApiResponse<>(false, null, message, errors);
    }
}
