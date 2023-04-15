package com.example.accountverification.exceptions;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@Builder
public class ApiResponse {
    private String path;
    private ZonedDateTime zonedDateTime;
    private HttpStatus httpStatus;
    private int statusCode;
    private String message;
}
