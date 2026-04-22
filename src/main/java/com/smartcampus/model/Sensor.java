package com.smartcampus.model;

/**
 * Represents a physical sensor device deployed in a campus room.
 *
 * Status values:
 *   "ACTIVE"      — operational, can accept new readings
 *   "MAINTENANCE" — offline for servicing; POST readings are blocked (403)
 *   "OFFLINE"     — powered down; readings are also blocked
 *
 * currentValue is updated every time a new SensorReading is successfully posted,
 * keeping the sensor record in sync with the latest measurement.
 */
public class Sensor {

    /** Unique identifier, e.g. "TEMP-001" */
    private String id;

    /** Category of sensor, e.g. "Temperature", "Occupancy", "CO2" */
    private String type;

    /** Current operational state: "ACTIVE", "MAINTENANCE", or "OFFLINE" */
    private String status;

    /** Most recent measurement value (updated on each new reading POST) */
    private double currentValue;

    /** Foreign key — the ID of the Room this sensor is installed in */
    private String roomId;

    // ---- Constructors ----

    /** No-arg constructor required by Jackson */
    public Sensor() {}

    public Sensor(String id, String type, String status, double currentValue, String roomId) {
        this.id           = id;
        this.type         = type;
        this.status       = status;
        this.currentValue = currentValue;
        this.roomId       = roomId;
    }

    // ---- Getters & Setters ----

    public String getId()                         { return id; }
    public void   setId(String id)                { this.id = id; }

    public String getType()                       { return type; }
    public void   setType(String type)            { this.type = type; }

    public String getStatus()                     { return status; }
    public void   setStatus(String status)        { this.status = status; }

    public double getCurrentValue()               { return currentValue; }
    public void   setCurrentValue(double v)       { this.currentValue = v; }

    public String getRoomId()                     { return roomId; }
    public void   setRoomId(String roomId)        { this.roomId = roomId; }
}
