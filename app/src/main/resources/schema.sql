CREATE TABLE IF NOT EXISTS formation (
    id          BIGSERIAL PRIMARY KEY,
    titre       VARCHAR(200) NOT NULL,
    date_debut  DATE         NOT NULL,
    places      INTEGER      NOT NULL CHECK (places > 0)
);

CREATE TABLE IF NOT EXISTS inscription (
    id            BIGSERIAL PRIMARY KEY,
    formation_id  BIGINT       NOT NULL REFERENCES formation(id) ON DELETE CASCADE,
    nom           VARCHAR(150) NOT NULL,
    email         VARCHAR(200) NOT NULL,
    CONSTRAINT uk_formation_email UNIQUE (formation_id, email)
);
