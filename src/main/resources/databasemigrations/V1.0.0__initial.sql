-- Create schemas

-- Create tables
CREATE TABLE IF NOT EXISTS GenerationRequests
(
    id SERIAL PRIMARY KEY,
    request_id VARCHAR(255),
    generation_count bigint,
    duration bigint,
    parameters text[][],
    map_type VARCHAR(255),
    game_type VARCHAR(255),
    timestamp_request timestamp,
    host VARCHAR(255),
    user_agent VARCHAR(255)
);


-- Create FKs

-- Create Indexes

