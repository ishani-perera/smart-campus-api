package com.smartcampus.mapper;

import com.smartcampus.model.ErrorResponse;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Converts JAX-RS runtime exceptions into JSON.
 *
 * This prevents framework-generated HTML error pages for situations such as:
 * - 404 Not Found
 * - 405 Method Not Allowed
 * - 415 Unsupported Media Type
 */
@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException exception) {
        Response original = exception.getResponse();
        int status = original != null ? original.getStatus() : 500;

        String error = "Request Error";
        String message = exception.getMessage() != null ? exception.getMessage() : "The request could not be processed.";

        if (exception instanceof NotFoundException) {
            error = "Not Found";
            message = "The requested endpoint or resource was not found.";
        } else if (exception instanceof NotAllowedException) {
            error = "Method Not Allowed";
            message = "The HTTP method is not allowed for this endpoint.";
        } else if (exception instanceof NotSupportedException) {
            error = "Unsupported Media Type";
            message = "Content-Type must be application/json for this endpoint.";
        } else if (status == 400) {
            error = "Bad Request";
            message = "The request could not be understood by the server.";
        }

        return Response
                .status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse(error, message, status))
                .build();
    }
}
