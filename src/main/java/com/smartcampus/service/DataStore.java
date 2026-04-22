package com.smartcampus.service;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Central in-memory data store for the Smart Campus API.
 *
 * WHY A SINGLETON?
 * JAX-RS creates a new instance of each Resource class per HTTP request.
 * If the data store were inside a resource class, every request would start
 * with an empty dataset — all previously created rooms/sensors would vanish.
 * By making DataStore a singleton (initialised once via a static instance),
 * data persists across all requests for the lifetime of the server.
 *
 * THREAD SAFETY:
 * We use ConcurrentHashMap instead of a plain HashMap. ConcurrentHashMap
 * allows multiple threads to read simultaneously and handles concurrent writes
 * safely without needing manual synchronisation blocks. For nested collections,
 * we also use CopyOnWriteArrayList so the lists themselves are thread-safe.
 *
 * STORAGE STRUCTURE:
 *   rooms    → Map<roomId,   Room>
 *   sensors  → Map<sensorId, Sensor>
 *   readings → Map<sensorId, List<SensorReading>>
 */
public class DataStore {

    private static final DataStore INSTANCE = new DataStore();

    public static DataStore getInstance() {
        return INSTANCE;
    }

    private DataStore() {
        seedDemoData();
    }

    /** All rooms, keyed by room ID */
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();

    /** All sensors, keyed by sensor ID */
    private final Map<String, Sensor> sensors = new ConcurrentHashMap<>();

    /** Sensor readings, keyed by sensorId */
    private final Map<String, List<SensorReading>> readings = new ConcurrentHashMap<>();

    public Map<String, Room>                getRooms()    { return rooms; }
    public Map<String, Sensor>              getSensors()  { return sensors; }
    public Map<String, List<SensorReading>> getReadings() { return readings; }

    /**
     * Convenience: get (or create) the readings list for a given sensorId.
     * computeIfAbsent safely initialises the list exactly once.
     */
    public List<SensorReading> getReadingsForSensor(String sensorId) {
        return readings.computeIfAbsent(sensorId, key -> new CopyOnWriteArrayList<>());
    }

    private void seedDemoData() {
        Room r1 = new Room("LIB-301", "Library Quiet Study", 40);
        Room r2 = new Room("LAB-101", "Computer Lab A", 30);
        rooms.put(r1.getId(), r1);
        rooms.put(r2.getId(), r2);

        Sensor s1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 21.5, "LIB-301");
        Sensor s2 = new Sensor("CO2-001",  "CO2",         "ACTIVE", 412.0, "LIB-301");
        Sensor s3 = new Sensor("OCC-001",  "Occupancy",   "MAINTENANCE", 0.0, "LAB-101");

        sensors.put(s1.getId(), s1);
        sensors.put(s2.getId(), s2);
        sensors.put(s3.getId(), s3);

        r1.getSensorIds().add(s1.getId());
        r1.getSensorIds().add(s2.getId());
        r2.getSensorIds().add(s3.getId());

        List<SensorReading> tempReadings = getReadingsForSensor("TEMP-001");
        tempReadings.add(new SensorReading("READ-0001", System.currentTimeMillis() - 60000, 20.8));
        tempReadings.add(new SensorReading("READ-0002", System.currentTimeMillis(), 21.5));
    }
}
