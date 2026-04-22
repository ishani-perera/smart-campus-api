package com.smartcampus.model;

/**
 * Represents a single timestamped measurement captured by a Sensor.
 *
 * A reading is immutable once recorded — you can retrieve readings but not
 * modify them. Each reading is linked to a sensor via the sub-resource path:
 *   POST /api/v1/sensors/{sensorId}/readings
 */
public class SensorReading {

    /** Unique event ID — generated as a UUID on creation */
    private String id;

    /** Epoch milliseconds when the reading was captured */
    private long timestamp;

    /** The actual metric value recorded by the hardware (e.g. 22.5 for °C) */
    private double value;

    // ---- Constructors ----

    /** No-arg constructor required by Jackson */
    public SensorReading() {}

    public SensorReading(String id, long timestamp, double value) {
        this.id        = id;
        this.timestamp = timestamp;
        this.value     = value;
    }

    // ---- Getters & Setters ----

    public String getId()                   { return id; }
    public void   setId(String id)          { this.id = id; }

    public long   getTimestamp()            { return timestamp; }
    public void   setTimestamp(long ts)     { this.timestamp = ts; }

    public double getValue()                { return value; }
    public void   setValue(double value)    { this.value = value; }
}
