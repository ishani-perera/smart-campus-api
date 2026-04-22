package com.smartcampus.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents a physical room on campus.
 *
 * A Room can contain zero or more Sensors. The sensorIds list tracks which
 * sensors have been deployed in this room. When a room is deleted, we first
 * check that this list is empty (business rule: no orphan sensors).
 */
public class Room {

    /** Unique identifier, e.g. "LIB-301" */
    private String id;

    /** Human-readable display name, e.g. "Library Quiet Study" */
    private String name;

    /** Maximum occupancy for safety regulations */
    private int capacity;

    /**
     * IDs of sensors currently deployed in this room.
     * A thread-safe list avoids race conditions when multiple requests add IDs.
     */
    private List<String> sensorIds = new CopyOnWriteArrayList<>();

    // ---- Constructors ----

    /** No-arg constructor required by Jackson for JSON deserialization */
    public Room() {}

    public Room(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    // ---- Getters & Setters ----

    public String getId()                       { return id; }
    public void   setId(String id)              { this.id = id; }

    public String getName()                     { return name; }
    public void   setName(String name)          { this.name = name; }

    public int    getCapacity()                 { return capacity; }
    public void   setCapacity(int capacity)     { this.capacity = capacity; }

    public List<String> getSensorIds()          { return sensorIds; }

    public void setSensorIds(List<String> sensorIds) {
        this.sensorIds = (sensorIds == null) ? new CopyOnWriteArrayList<>() : new CopyOnWriteArrayList<>(sensorIds);
    }
}
