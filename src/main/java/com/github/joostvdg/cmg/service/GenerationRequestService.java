package com.github.joostvdg.cmg.service;

import com.github.joostvdg.cmg.analytics.GenerationRequest;

import java.util.List;
import java.util.Optional;

/**
 * All interaction for the GenerationRequest table.
 */
public interface GenerationRequestService {

    /**
     * Finds all existing GenerationRequest's in the database.
     * @return the list of GenerationRequest
     */
    List<GenerationRequest> findAll();

    /**
     * Creates a new GenerationRequest in the database.
     * @param generationRequest the data for the GenerationRequest to store
     * @return the GenerationRequest with the database Id
     */
    GenerationRequest create(GenerationRequest generationRequest);

    /**
     * Attemps to find a GenerationRequest by its Id
     * @param id the database Id of the GenerationRequest
     * @return the GenerationRequest or null if not found
     */
    Optional<GenerationRequest> findById(int id);

    /**
     * Attempts to delete a GenerationRequest by its database Id
     * @param id the GenerationRequest's database Id
     * @return true if a record was deleted, false if not
     */
    boolean deleteById(int id);

    /**
     * Updates and existing GenerationRequest
     * @param generationRequest the GenerationRequest to update
     * @return true if a record was updated, false if not
     */
    boolean update(GenerationRequest generationRequest);
}
