package com.ecommerce.user.payloads;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse {

    private String message;
    private HttpStatus httpStatus;
    private Object data;
}
