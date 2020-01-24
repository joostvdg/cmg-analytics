package com.github.joostvdg.cmg.analytics;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDateTime;
import java.util.*;

@RegisterForReflection
public class GenerationRequest {
    private String requestId;
    private long generationCount;
    private long duration;
    private List<String> parameters;
    private String mapType;
    private String gameType;
    private LocalDateTime timestamp;
    private String host;
    private String userAgent;

    private GenerationRequest(Builder builder) {
        this.requestId = builder.requestId;
        this.generationCount = builder.generationCount;
        this.duration = builder.duration;
        this.parameters = builder.parameters;
        this.mapType = builder.mapType;
        this.gameType = builder.gameType;
        this.timestamp = builder.timestamp;
        this.host = builder.host;
        this.userAgent = builder.userAgent;
    }

    public String getRequestId() {
        return requestId;
    }

    public long getGenerationCount() {
        return generationCount;
    }

    public long getDuration() {
        return duration;
    }

    public List<String> getParameters() {
        List<String> extParameters = new ArrayList<>();
        parameters.forEach(item -> extParameters.add(item));
        return extParameters;
    }

    public String getMapType() {
        return mapType;
    }

    public String getGameType() {
        return gameType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getHost() {
        return host;
    }

    public String getUserAgent() {
        return userAgent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenerationRequest that = (GenerationRequest) o;
        return Objects.equals(parameters, that.parameters) &&
                Objects.equals(mapType, that.mapType) &&
                Objects.equals(gameType, that.gameType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameters, mapType, gameType);
    }

    @Override
    public String toString() {
        return "GenerationRequest{" +
                "requestId='" + requestId + '\'' +
                ", generationCount=" + generationCount +
                ", duration=" + duration +
                ", parameters=" + parameters +
                ", mapType='" + mapType + '\'' +
                ", gameType='" + gameType + '\'' +
                ", timestamp=" + timestamp +
                ", host='" + host + '\'' +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }

    @JsonCreator
    public static GenerationRequest createGenerationRequest(String requestId, long generationCount, long duration, List<String> parameters, String mapType, String gameType, LocalDateTime timestamp, String host, String userAgent) {
        Builder builder = new Builder();
        builder.requestId = requestId;
        builder.generationCount = generationCount;
        builder.duration = duration;
        builder.parameters = parameters;
        builder.mapType = mapType;
        builder.gameType = gameType;
        builder.timestamp = timestamp;
        builder.host = host;
        builder.userAgent = userAgent;
        return new GenerationRequest(builder);
    }

    public static class Builder {
        private String requestId;
        private long generationCount;
        private long duration;
        private List<String> parameters;
        private String mapType;
        private String gameType;
        private LocalDateTime timestamp;
        private String host;
        private String userAgent;

        public Builder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder generationCount(long generationCount) {
            this.generationCount = generationCount;
            return this;
        }

        public Builder duration(long duration) {
            this.duration = duration;
            return this;
        }

        public Builder parameters(List<String> parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder mapType(String mapType) {
            this.mapType = mapType;
            return this;
        }

        public Builder gameType(String gameType) {
            this.gameType = gameType;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public GenerationRequest build() {
            return new GenerationRequest(this);
        }
    }


}
