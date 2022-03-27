CREATE SEQUENCE IF NOT EXISTS id_sequence START WITH 1;
CREATE TABLE IF NOT EXISTS btc_record (
    id                     LONG  CONSTRAINT btc_record_pkey PRIMARY KEY,
    date_time              TIMESTAMP WITH TIME ZONE     NOT NULL,
    amount                 DOUBLE      NOT NULL
);
CREATE INDEX IF NOT EXISTS date_time_index ON btc_record(date_time);