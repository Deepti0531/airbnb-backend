package com.projects.airBnbApp.advice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private ApiError error;

    // Success constructor
    public ApiResponse(T data) {
        this.success = true;
        this.data = data;
        this.error = null;
    }

    // Error constructor (NOTE: ApiError not Object!)
    public ApiResponse(ApiError error) {
        this.success = false;
        this.data = null;
        this.error = error;
    }
}
