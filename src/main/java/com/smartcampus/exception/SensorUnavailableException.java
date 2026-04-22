package com.smartcampus.exception;

/**
 * Thrown when a reading is posted to a sensor that cannot accept data,
 * e.g. a sensor with status "MAINTENANCE" or "OFFLINE".
 *
 * Mapped to HTTP 403 Forbidden by SensorUnavailableExceptionMapper.
 *
 * 403 Forbidden is appropriate here because the server understands the request
 * perfectly, but is refusing to honour it due to the sensor's current state.
 * The client is not authorised to post readings to a sensor that is not ACTIVE.
 */
public class SensorUnavailableException extends RuntimeException {

    public SensorUnavailableException(String message) {
        super(message);
    }
}
