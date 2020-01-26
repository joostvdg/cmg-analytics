package com.github.joostvdg.cmg.resource;

import com.github.joostvdg.cmg.analytics.GenerationRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/generationRequest")
public class GenerationRequestResource {

    private static final Logger LOG = LoggerFactory.getLogger(GenerationRequestResource.class);

    protected final DSLContext dslContext;

    public GenerationRequestResource(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Get
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse generationRequestDemo() {
//        DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);
        dslContext.query("select * from generationrequests;");

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

        return HttpResponse.ok().contentType(MediaType.APPLICATION_JSON_TYPE).body(generationRequest);
    }

    @Post
    @Consumes(MediaType.APPLICATION_JSON)
    public HttpResponse receiveGenerationRequest(@Body GenerationRequest generationRequest) {
        if (generationRequest == null ) {
            LOG.info("Request failed, no content");
            return HttpResponse.status(HttpStatus.BAD_REQUEST, "No valid object");
        }
        LOG.info("Request received: " + generationRequest.toString());
        return HttpResponse.noContent();
    }
}
