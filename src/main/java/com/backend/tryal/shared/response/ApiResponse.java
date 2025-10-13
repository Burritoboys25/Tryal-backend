package com.backend.tryal.shared.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private String status;
//    private String message;
    private T data;
    // metadata could be used for optional information (eg., pagination details) -- not sure if it'll be useful later
    // private Object metadata;
    public ApiResponse(T data) {
      this.status = "success";
      this.data = data;
    }
}
