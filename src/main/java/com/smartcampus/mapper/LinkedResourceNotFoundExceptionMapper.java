package com.smartcampus.mapper;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.model.ErrorResponse;

import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Part 5.2 — Maps LinkedResourceNotFoundException to HTTP 422 Unprocessable Entity.
 *
 * Triggered when a sensor is created with a roomId that doesn't exist.
 * Returns a structured JSON error so the client knows exactly which field
 * caused the validation failure.
 */
@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {
        ErrorResponse error = new ErrorResponse(
                "Linked Resource Not Found",
                exception.getMessage(),
                422
        );

        // 422 Unprocessable Entity — the request syntax is valid, but the
        // semantic content (the roomId reference) cannot be processed.
        return Response
                .status(422)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
