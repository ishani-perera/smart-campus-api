package com.smartcampus.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Part 5.5 — API Request & Response Logging Filter
 *
 * This single class implements BOTH filter interfaces, handling both the
 * inbound request and outbound response in one place.
 *
 * By implementing ContainerRequestFilter  → logs incoming requests
 * By implementing ContainerResponseFilter → logs outgoing responses
 *
 * The @Provider annotation registers the filter with the JAX-RS runtime.
 * JAX-RS automatically invokes these methods for EVERY request/response
 * without any changes to the resource classes themselves.
 *
 * WHY USE FILTERS FOR LOGGING? (Report Answer Part 5.5):
 * Filters implement the "cross-cutting concern" pattern. Logging is needed
 * on every endpoint, but it has nothing to do with business logic. If we
 * manually added Logger.info() to every resource method:
 *
 * 1. CODE DUPLICATION — We'd repeat the same logging code in dozens of methods.
 * 2. MAINTENANCE BURDEN — Changing the log format means editing every method.
 * 3. SEPARATION OF CONCERNS VIOLATION — Resource methods should only handle
 *    business logic, not infrastructure concerns like observability.
 *
 * A filter is written once, registered once, and runs automatically for all
 * endpoints — past, present, and future — with zero changes to resource code.
 * This is a core principle of Aspect-Oriented Programming (AOP).
 */
@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());

    /**
     * Called BEFORE the request reaches the resource method.
     * Logs the HTTP method and full request URI.
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        LOGGER.info(String.format(
                "--> INCOMING REQUEST  | Method: %-6s | URI: %s",
                requestContext.getMethod(),
                requestContext.getUriInfo().getRequestUri()
        ));
    }

    /**
     * Called AFTER the resource method has produced a response.
     * Logs the HTTP method, URI, and final HTTP status code.
     */
    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {
        LOGGER.info(String.format(
                "<-- OUTGOING RESPONSE | Method: %-6s | URI: %s | Status: %d",
                requestContext.getMethod(),
                requestContext.getUriInfo().getRequestUri(),
                responseContext.getStatus()
        ));
    }
}
