package com.smartcampus.exception;

/**
 * Thrown when a resource references another resource that doesn't exist.
 * Primary use case: creating a Sensor with a roomId that does not exist.
 *
 * Mapped to HTTP 422 Unprocessable Entity by LinkedResourceNotFoundExceptionMapper.
 *
 * WHY 422 AND NOT 404? (Report Answer Part 5.2):
 * HTTP 404 means "the URL you requested does not exist on this server" — i.e.,
 * the endpoint /api/v1/sensors is perfectly valid. The problem is INSIDE the
 * request payload: the value of the roomId field references a room that doesn't
 * exist. HTTP 422 Unprocessable Entity means "the request was well-formed and
 * the server understands the content type, but was unable to process the contained
 * instructions." This is far more semantically accurate — the JSON is valid,
 * the route exists, but a business rule (referential integrity) was violated.
 * Using 404 here would confuse clients into thinking the /sensors endpoint itself
 * is missing, not that a referenced resource within the payload is absent.
 */
public class LinkedResourceNotFoundException extends RuntimeException {

    public LinkedResourceNotFoundException(String message) {
        super(message);
    }
}
