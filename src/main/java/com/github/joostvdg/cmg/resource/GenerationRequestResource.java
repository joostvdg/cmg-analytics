package com.github.joostvdg.cmg.resource;

import com.github.joostvdg.cmg.analytics.GenerationRequest;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.logging.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Path("/generationRequest")
public class GenerationRequestResource {

    private static final Logger LOG = Logger.getLogger(GenerationRequestResource.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response generationRequestDemo() {
        var generationRequest = new GenerationRequest.Builder()
            .generationCount(100)
            .duration(100)
            .gameType("Normal")
            .mapType("Standard")
            .requestId(UUID.randomUUID().toString())
            .timestamp(LocalDateTime.now())
            .host("Localhost")
            .userAgent("MeMyselfAndI")
            .parameters(Collections.emptyList())
            .build();
        return Response.ok(generationRequest, MediaType.APPLICATION_JSON_TYPE).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response receiveGenerationRequest(@RequestBody GenerationRequest generationRequest) {
        if (generationRequest == null ) {
            LOG.infof("Request failed, no content");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "No valid object").build();
        }
        LOG.infof("Request received: %s", generationRequest.toString());
        return Response.noContent().build();
    }
}
