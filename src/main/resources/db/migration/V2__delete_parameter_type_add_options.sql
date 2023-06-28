ALTER TABLE alert_tg_bot.parameters_data DROP COLUMN IF EXISTS param_type;
DROP TYPE IF EXISTS alert_tg_bot.parameter_type;
ALTER TABLE alert_tg_bot.parameters_data
    ADD COLUMN IF NOT EXISTS options_values CHARACTER VARYING;
ALTER TABLE alert_tg_bot.parameters_data
    ADD COLUMN IF NOT EXISTS mandatory BOOL DEFAULT false;
ALTER TABLE alert_tg_bot.parameters_data
    ADD COLUMN IF NOT EXISTS value_regexp CHARACTER VARYING;