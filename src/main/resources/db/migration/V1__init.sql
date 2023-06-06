CREATE SCHEMA IF NOT EXISTS alert_tg_bot;

CREATE TABLE alert_tg_bot.state_data
(
    id           BIGSERIAL               NOT NULL,
    user_id      BIGINT                  NOT NULL,
    alert_id     CHARACTER VARYING,
    map_params   CHARACTER VARYING,

    CONSTRAINT state_data_pkey PRIMARY KEY (id),
    UNIQUE (user_id)
);

CREATE TYPE alert_tg_bot.parameter_type AS ENUM ('bl', 'fl', 'integer', 'str');

CREATE TABLE alert_tg_bot.parameters
(
    id           BIGSERIAL                   NOT NULL,
    alert_id     CHARACTER VARYING           NOT NULL,
    param_id     CHARACTER VARYING           NOT NULL,
    param_name   CHARACTER VARYING           NOT NULL,
    param_type   alert_tg_bot.parameter_type NOT NULL,

    CONSTRAINT parameters_pkey PRIMARY KEY (id),
    UNIQUE (param_id)
);
