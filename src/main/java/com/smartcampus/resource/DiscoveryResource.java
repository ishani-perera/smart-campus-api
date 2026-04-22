package com.smartcampus.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Part 1.2 — Discovery / Root Endpoint
 *
 * GET /api/v1
 *
 * Returns API metadata including version, admin contact, and a map of primary
 * resource URIs. This implements a basic form of HATEOAS (Hypermedia As The
 * Engine Of Application State) — the response tells clients where to find
 * the main resources without them needing to consult external documentation.
 *
 * HATEOAS REPORT ANSWER (Part 1.2):
 * HATEOAS is considered a hallmark of advanced REST design because it makes
 * APIs self-documenting and navigable. Instead of a client having to read
 * static docs to know the URL for rooms, the Discovery response embeds that
 * link directly. This means clients can start from a single known URL (/api/v1)
 * and discover everything else dynamically — reducing coupling between the
 * client and the API's URL structure. If URLs change, the server updates the
 * links in the response and clients automatically adapt, without needing
 * updated documentation.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {

    @GET
    public Response discover() {
        // Build the discovery response as a simple map — Jackson converts it to JSON
        Map<String, Object> response = new HashMap<>();

        response.put("apiName",    "Smart Campus Sensor & Room Management API");
        response.put("version",    "1.0.0");
        response.put("adminContact", "admin@smartcampus.ac.uk");
        response.put("description", "RESTful API for managing campus rooms and IoT sensors");

        // Resource navigation links (HATEOAS)
        Map<String, String> resources = new HashMap<>();
        resources.put("rooms",   "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");
        response.put("resources", resources);

        // Extra metadata
        response.put("status", "UP");
        response.put("timestamp", System.currentTimeMillis());

        return Response.ok(response).build();
    }
}
