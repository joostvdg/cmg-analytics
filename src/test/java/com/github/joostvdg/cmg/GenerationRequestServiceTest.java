package com.github.joostvdg.cmg;

import com.github.joostvdg.cmg.analytics.GenerationRequest;
import com.github.joostvdg.cmg.service.GenerationRequestService;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@MicronautTest
public class GenerationRequestServiceTest {

    @Inject
    GenerationRequestService generationRequestService;

    @Test
    void testWeCanRetrieveRecords(){
        var result = generationRequestService.findAll();
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertTrue(result.size() > 0);
    }

    @Test
    void testCRUD() {
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

        var createdGenerationRequest = generationRequestService.create(generationRequest);
        Assertions.assertNotNull(createdGenerationRequest);
        Assertions.assertEquals(generationRequest.getDuration(), createdGenerationRequest.getDuration());

        var foundGenerationRequest = generationRequestService.findById(createdGenerationRequest.getId());
        Assertions.assertTrue(foundGenerationRequest.isPresent());
        Assertions.assertEquals(generationRequest.getDuration(), foundGenerationRequest.get().getDuration());

        var isDeleted = generationRequestService.deleteById(foundGenerationRequest.get().getId());
        Assertions.assertTrue(isDeleted);

        foundGenerationRequest = generationRequestService.findById(createdGenerationRequest.getId());
        Assertions.assertTrue(foundGenerationRequest.isEmpty());
    }
}
