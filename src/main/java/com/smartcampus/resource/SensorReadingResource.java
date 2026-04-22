package com.smartcampus.resource;

import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.service.DataStore;
import com.smartcampus.util.ApiResponses;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Manages readings for a single sensor sub-resource.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final DataStore store = DataStore.getInstance();
    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public Response getAllReadings() {
        Sensor sensor = store.getSensors().get(sensorId);
        if (sensor == null) {
            return ApiResponses.error(Response.Status.NOT_FOUND, "Not Found", "Sensor '" + sensorId + "' does not exist.");
        }

        List<SensorReading> readingList = store.getReadingsForSensor(sensorId);
        readingList.sort(Comparator.comparingLong(SensorReading::getTimestamp));
        return Response.ok(readingList).build();
    }

    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = store.getSensors().get(sensorId);
        if (sensor == null) {
            return ApiResponses.error(Response.Status.NOT_FOUND, "Not Found", "Sensor '" + sensorId + "' does not exist.");
        }

        if (reading == null) {
            return ApiResponses.error(Response.Status.BAD_REQUEST, "Bad Request", "Request body is required when creating a sensor reading.");
        }

        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(
                    "Sensor '" + sensorId + "' is currently under MAINTENANCE and cannot accept new readings."
            );
        }

        if ("OFFLINE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(
                    "Sensor '" + sensorId + "' is OFFLINE and cannot accept new readings."
            );
        }

        if (reading.getId() == null || reading.getId().isBlank()) {
            reading.setId("READ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        if (reading.getTimestamp() <= 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        List<SensorReading> readings = store.getReadingsForSensor(sensorId);
        boolean duplicateReadingId = readings.stream()
                .anyMatch(existing -> existing.getId() != null && existing.getId().equalsIgnoreCase(reading.getId()));
        if (duplicateReadingId) {
            return ApiResponses.error(Response.Status.CONFLICT, "Conflict", "A reading with ID '" + reading.getId() + "' already exists for sensor '" + sensorId + "'.");
        }

        readings.add(reading);
        sensor.setCurrentValue(reading.getValue());

        URI location = URI.create("/api/v1/sensors/" + sensorId + "/readings/" + reading.getId());
        return Response
                .status(Response.Status.CREATED)
                .header("Location", location)
                .entity(reading)
                .build();
    }
}
