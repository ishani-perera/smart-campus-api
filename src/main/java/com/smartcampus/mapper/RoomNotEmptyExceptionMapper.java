package com.smartcampus.mapper;

import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.ErrorResponse;

import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Part 5.1 — Maps RoomNotEmptyException to HTTP 409 Conflict.
 *
 * The @Provider annotation registers this mapper with the JAX-RS runtime.
 * Whenever a RoomNotEmptyException is thrown anywhere in the application,
 * JAX-RS intercepts it and calls toResponse() instead of propagating it
 * as a raw 500 error.
 */
@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        ErrorResponse error = new ErrorResponse(
                "Room Not Empty",       // short label
                exception.getMessage(), // the specific message we set when throwing
                409
        );

        return Response
                .status(Response.Status.CONFLICT)   // 409 Conflict
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
