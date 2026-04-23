package com.smartcampus.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.model.Sensor;
import com.smartcampus.service.DataStore;
import com.smartcampus.util.ApiResponses;

/**
 * Manages the /api/v1/sensors collection.
 */
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private final DataStore store = DataStore.getInstance();

    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> sensorList = new ArrayList<>(store.getSensors().values());

        if (type != null && !type.isBlank()) {
            sensorList = sensorList.stream()
                    .filter(sensor -> sensor.getType() != null && sensor.getType().equalsIgnoreCase(type.trim()))
                    .collect(Collectors.toList());
        }

        sensorList.sort(Comparator.comparing(Sensor::getId, String.CASE_INSENSITIVE_ORDER));
        return Response.ok(sensorList).build();
    }

    @POST
    public Response createSensor(Sensor sensor) {
        if (sensor == null) {
            return ApiResponses.error(Response.Status.BAD_REQUEST, "Bad Request", "Request body is required.");
        }

        if (sensor.getType() == null || sensor.getType().isBlank()) {
            return ApiResponses.error(Response.Status.BAD_REQUEST, "Bad Request", "Sensor 'type' is required.");
        }

        if (sensor.getRoomId() == null || sensor.getRoomId().isBlank()) {
            return ApiResponses.error(Response.Status.BAD_REQUEST, "Bad Request", "Sensor 'roomId' is required.");
        }

        if (!store.getRooms().containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException(
                    "Cannot create sensor because room with ID '" + sensor.getRoomId() + "' does not exist."
            );
        }

        if (sensor.getId() == null || sensor.getId().isBlank()) {
            sensor.setId("SENSOR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        if (store.getSensors().containsKey(sensor.getId())) {
            return ApiResponses.error(Response.Status.CONFLICT, "Conflict", "A sensor with ID '" + sensor.getId() + "' already exists.");
        }

        String status = sensor.getStatus();
        if (status == null || status.isBlank()) {
            status = "ACTIVE";
        }

        status = status.trim().toUpperCase(Locale.ROOT);
        if (!status.equals("ACTIVE") && !status.equals("MAINTENANCE") && !status.equals("OFFLINE")) {
            return ApiResponses.error(Response.Status.BAD_REQUEST, "Bad Request", "Sensor 'status' must be ACTIVE, MAINTENANCE, or OFFLINE.");
        }

        sensor.setStatus(status);
        sensor.setType(sensor.getType().trim());
        sensor.setRoomId(sensor.getRoomId().trim());

        store.getSensors().put(sensor.getId(), sensor);
        store.getRooms().get(sensor.getRoomId()).getSensorIds().add(sensor.getId());

        URI location = URI.create("/api/v1/sensors/" + sensor.getId());
        return Response
                .status(Response.Status.CREATED)
                .header("Location", location)
                .entity(sensor)
                .build();
    }

    @GET
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = store.getSensors().get(sensorId);
        if (sensor == null) {
            return ApiResponses.error(Response.Status.NOT_FOUND, "Not Found", "Sensor with ID '" + sensorId + "' does not exist.");
        }
        return Response.ok(sensor).build();
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }

    @GET
    @Path("/debug/error")
    public Response triggerServerError() {
        throw new RuntimeException("Forced demo error for GlobalExceptionMapper testing.");
    }
}