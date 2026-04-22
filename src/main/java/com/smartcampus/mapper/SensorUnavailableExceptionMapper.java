package com.smartcampus.mapper;

import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.ErrorResponse;

import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Part 5.3 — Maps SensorUnavailableException to HTTP 403 Forbidden.
 *
 * Triggered when a client tries to POST a reading to a sensor whose
 * status is "MAINTENANCE" or "OFFLINE". The server understands the
 * request but refuses to honour it due to the sensor's current state.
 */
@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException exception) {
        ErrorResponse error = new ErrorResponse(
                "Sensor Unavailable",
                exception.getMessage(),
                403
        );

        return Response
                .status(Response.Status.FORBIDDEN)   // 403 Forbidden
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
