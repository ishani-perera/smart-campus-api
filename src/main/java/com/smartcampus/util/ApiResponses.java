package com.smartcampus.util;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.smartcampus.model.ErrorResponse;

/**
 * Small helper for building consistent JSON responses.
 */
public final class ApiResponses {

    private ApiResponses() {
    }

    public static Response error(Response.Status status, String error, String message) {
        return Response
                .status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse(error, message, status.getStatusCode()))
                .build();
    }

    public static Response error(int statusCode, String error, String message) {
        return Response
                .status(statusCode)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse(error, message, statusCode))
                .build();
    }
}
