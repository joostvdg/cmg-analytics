package com.github.joostvdg.cmg.resource;

import com.github.joostvdg.cmg.analytics.GenerationRequest;
import com.github.joostvdg.cmg.service.GenerationRequestService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/generationRequest")
public class GenerationRequestResource {

    private static final Logger LOG = LoggerFactory.getLogger(GenerationRequestResource.class);


    private GenerationRequestService generationRequestService;

    public GenerationRequestResource(GenerationRequestService generationRequestService) {
        this.generationRequestService = generationRequestService;
    }

    @Get
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse generationRequestDemo() {
        var generationRequests = generationRequestService.findAll();
        return HttpResponse.ok().contentType(MediaType.APPLICATION_JSON_TYPE).body(generationRequests);
    }

    @Post
    @Consumes(MediaType.APPLICATION_JSON)
    public HttpResponse receiveGenerationRequest(@Body GenerationRequest generationRequest) {
        if (generationRequest == null ) {
            LOG.info("Request failed, no content");
            return HttpResponse.status(HttpStatus.BAD_REQUEST, "No valid object");
        }
        LOG.info("Request received: " + generationRequest.toString());
        var databaseResponse = generationRequestService.create(generationRequest);

        if(databaseResponse != null) {
            return HttpResponse.created(generationRequest);
        }
        return HttpResponse.noContent();
    }
}
