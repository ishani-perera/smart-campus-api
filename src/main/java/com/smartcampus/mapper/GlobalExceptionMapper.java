package com.smartcampus.mapper;

import com.smartcampus.model.ErrorResponse;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Final safety net for unexpected runtime failures.
 *
 * This mapper only handles truly unexpected exceptions. JAX-RS framework-level
 * HTTP exceptions are handled separately by WebApplicationExceptionMapper so
 * they can keep their original status codes while still returning JSON.
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        LOGGER.log(Level.SEVERE, "Unhandled exception caught by global mapper", exception);

        ErrorResponse error = new ErrorResponse(
                "Internal Server Error",
                "An unexpected error occurred. Please contact the system administrator.",
                500
        );

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
