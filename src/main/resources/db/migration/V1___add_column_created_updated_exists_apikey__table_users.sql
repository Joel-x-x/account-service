ALTER TABLE users
    ADD COLUMN created TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE users
    ADD COLUMN updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE users
    ADD COLUMN exists BOOLEAN DEFAULT TRUE;
ALTER TABLE users
    ADD COLUMN api_key String;