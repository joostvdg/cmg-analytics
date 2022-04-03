package com.github.joostvdg.cmg.service.impl;

import com.github.joostvdg.cmg.analytics.GenerationRequest;
import com.github.joostvdg.cmg.service.GenerationRequestService;
import jakarta.inject.Singleton;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.github.joostvdg.cmg.analytics.tables.Generationrequests.GENERATIONREQUESTS;

/**
 * The implementation of GenerationRequestService.
 */
@Singleton
public class GenerationRequestServiceImpl implements GenerationRequestService {

    private static final Logger LOG = LoggerFactory.getLogger(GenerationRequestServiceImpl.class);

    /**
     * The JOOQ database context
     */
    protected final DSLContext dslContext;

    /**
     * The default contructor, we require the JOOQ database context.
     * Should be autowired by the Micronaut framework
     * @param dslContext the JOOQ context
     */
    public GenerationRequestServiceImpl(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    @Transactional
    public List<GenerationRequest> findAll() {
        List<GenerationRequest> generationRequests = new ArrayList<>();
        var records = dslContext.select().from(GENERATIONREQUESTS).fetch();

        for(Record record : records) {
            GenerationRequest generationRequest = fromRecord(record);
            generationRequests.add(generationRequest);
        }

        return generationRequests;
    }

    @Override
    @Transactional
    public GenerationRequest create(GenerationRequest generationRequest) {
        if (generationRequest == null) {
            throw new IllegalArgumentException("No valid generationRequest provided");
        }

        String[] parameters = new String[0];
        if (generationRequest.getParameters() != null || !generationRequest.getParameters().isEmpty()) {
            parameters = generationRequest.getParameters().toArray(new String[0] );
        }

        Record record = dslContext.insertInto(GENERATIONREQUESTS)
                .set(GENERATIONREQUESTS.REQUEST_ID, generationRequest.getRequestId())
                .set(GENERATIONREQUESTS.GENERATION_COUNT, generationRequest.getGenerationCount())
                .set(GENERATIONREQUESTS.DURATION, generationRequest.getDuration())
                .set(GENERATIONREQUESTS.MAP_TYPE, generationRequest.getMapType())
                .set(GENERATIONREQUESTS.GAME_TYPE, generationRequest.getGameType())
                .set(GENERATIONREQUESTS.HOST, generationRequest.getHost())
                .set(GENERATIONREQUESTS.USER_AGENT, generationRequest.getUserAgent())
                .set(GENERATIONREQUESTS.PARAMETERS, parameters)
                .set(GENERATIONREQUESTS.TIMESTAMP_REQUEST, generationRequest.getTimestamp())
                .returning(GENERATIONREQUESTS.ID)
                .fetchOne();

        if (record == null ) {
            LOG.warn("Insert did not return a valid result");
        } else {
            int id = record.get(GENERATIONREQUESTS.ID);
            generationRequest.updateId(id);
        }

        return generationRequest;
    }

    @Override
    public Optional<GenerationRequest> findById(int id) {
        var record = dslContext.fetchOne(GENERATIONREQUESTS, GENERATIONREQUESTS.ID.eq(id));

        GenerationRequest generationRequest = null;
        if (record != null) {
            generationRequest = fromRecord(record);
        }

        return Optional.ofNullable(generationRequest);
    }

    @Override
    public boolean deleteById(int id) {
        var record = dslContext.fetchOne(GENERATIONREQUESTS, GENERATIONREQUESTS.ID.eq(id));
        if (record != null) {
            record.delete();
            return true;
        }
        return false;
    }

    @Override
    public boolean update(GenerationRequest generationRequest) {
        if (generationRequest == null || generationRequest.getId() < 1) { // database Ids start at 1
            throw new IllegalArgumentException("No valid generationRequest provided");
        }

        var record = dslContext.fetchOne(GENERATIONREQUESTS, GENERATIONREQUESTS.ID.eq(generationRequest.getId()));
        if (record == null) {
            return false;
        }

        String[] parameters = new String[0];
        if (generationRequest.getParameters() != null || !generationRequest.getParameters().isEmpty()) {
            parameters = generationRequest.getParameters().toArray(new String[0] );
        }

        record.set(GENERATIONREQUESTS.REQUEST_ID, generationRequest.getRequestId());
        record.set(GENERATIONREQUESTS.GENERATION_COUNT, generationRequest.getGenerationCount());
        record.set(GENERATIONREQUESTS.DURATION, generationRequest.getDuration());
        record.set(GENERATIONREQUESTS.MAP_TYPE, generationRequest.getMapType());
        record.set(GENERATIONREQUESTS.GAME_TYPE, generationRequest.getGameType());
        record.set(GENERATIONREQUESTS.HOST, generationRequest.getHost());
        record.set(GENERATIONREQUESTS.USER_AGENT, generationRequest.getUserAgent());
        record.set(GENERATIONREQUESTS.PARAMETERS, parameters);
        record.set(GENERATIONREQUESTS.TIMESTAMP_REQUEST, generationRequest.getTimestamp());
        record.store();

        return true;
    }

    /**
     * Creates a GenerationRequest from a JOOQ Record of the GenerationRequest table
     * @param record the JOOQ Record
     * @return the created GenerationRequest
     */
    private GenerationRequest fromRecord(Record record) {
        int id = record.get(GENERATIONREQUESTS.ID);
        String requestId = record.get(GENERATIONREQUESTS.REQUEST_ID);
        long generationCount = record.get(GENERATIONREQUESTS.GENERATION_COUNT);
        long duration = record.get(GENERATIONREQUESTS.DURATION);
        String mapType = record.get(GENERATIONREQUESTS.MAP_TYPE);
        String gameType = record.get(GENERATIONREQUESTS.GAME_TYPE);
        String host = record.get(GENERATIONREQUESTS.HOST);
        String userAgent = record.get(GENERATIONREQUESTS.USER_AGENT);
        List<String> parameters = Arrays.asList(record.get(GENERATIONREQUESTS.PARAMETERS));
        LocalDateTime timestamp = record.get(GENERATIONREQUESTS.TIMESTAMP_REQUEST);

        return new GenerationRequest.Builder()
                .id(id)
                .requestId(requestId)
                .generationCount(generationCount)
                .duration(duration)
                .mapType(mapType)
                .gameType(gameType)
                .host(host)
                .userAgent(userAgent)
                .parameters(parameters)
                .timestamp(timestamp)
                .build();
    }
}
