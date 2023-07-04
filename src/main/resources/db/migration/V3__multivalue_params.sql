ALTER TABLE alert_tg_bot.parameters_data
    ADD COLUMN IF NOT EXISTS multiple_values BOOL DEFAULT false;