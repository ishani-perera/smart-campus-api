package com.smartcampus.model;

/**
 * Standard JSON error body returned by the API.
 *
 * All error responses should follow the same structure so clients can parse
 * them consistently, whether the error comes from business logic, validation,
 * or the JAX-RS runtime itself.
 */
public class ErrorResponse {

    private String error;
    private String message;
    private int status;
    private long timestamp;

    /** Required by Jackson */
    public ErrorResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    public ErrorResponse(String error, String message, int status) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.timestamp = System.currentTimeMillis();
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
