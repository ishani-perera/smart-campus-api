package com.smartcampus.resource;

import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.Room;
import com.smartcampus.service.DataStore;
import com.smartcampus.util.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Handles the /api/v1/rooms collection.
 */
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private final DataStore store = DataStore.getInstance();

    @GET
    public Response getAllRooms() {
        List<Room> roomList = new ArrayList<>(store.getRooms().values());
        roomList.sort(Comparator.comparing(Room::getId, String.CASE_INSENSITIVE_ORDER));
        return Response.ok(roomList).build();
    }

    @POST
    public Response createRoom(Room room) {
        if (room == null) {
            return ApiResponses.error(Response.Status.BAD_REQUEST, "Bad Request", "Request body is required.");
        }

        if (room.getName() == null || room.getName().isBlank()) {
            return ApiResponses.error(Response.Status.BAD_REQUEST, "Bad Request", "Room 'name' is required.");
        }

        if (room.getCapacity() < 0) {
            return ApiResponses.error(Response.Status.BAD_REQUEST, "Bad Request", "Room 'capacity' cannot be negative.");
        }

        if (room.getId() == null || room.getId().isBlank()) {
            room.setId("ROOM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        if (store.getRooms().containsKey(room.getId())) {
            return ApiResponses.error(Response.Status.CONFLICT, "Conflict", "A room with ID '" + room.getId() + "' already exists.");
        }

        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }

        store.getRooms().put(room.getId(), room);

        URI location = URI.create("/api/v1/rooms/" + room.getId());
        return Response
                .status(Response.Status.CREATED)
                .header("Location", location)
                .entity(room)
                .build();
    }

    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = store.getRooms().get(roomId);
        if (room == null) {
            return ApiResponses.error(Response.Status.NOT_FOUND, "Not Found", "Room with ID '" + roomId + "' does not exist.");
        }
        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = store.getRooms().get(roomId);
        if (room == null) {
            return ApiResponses.error(Response.Status.NOT_FOUND, "Not Found", "Room with ID '" + roomId + "' does not exist.");
        }

        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(
                    "Room '" + roomId + "' cannot be deleted because it still has "
                            + room.getSensorIds().size()
                            + " sensor(s) assigned. Remove or reassign those sensors first."
            );
        }

        store.getRooms().remove(roomId);
        return Response.noContent().build();
    }
}
