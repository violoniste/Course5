-- liquibase formatted sql

-- changeset iron:1
CREATE TABLE entry (
    id SERIAL,
    chat_id BIGINT,
    notification_text TEXT,
    date_time TIMESTAMP
)

