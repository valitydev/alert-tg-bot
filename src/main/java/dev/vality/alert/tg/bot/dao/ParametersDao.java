package dev.vality.alert.tg.bot.dao;

import dev.vality.alert.tg.bot.domain.tables.pojos.Parameters;

public interface ParametersDao {

    Parameters save(Parameters param);

    Parameters getByAlertIdAndParamName(String alertId, String paramName);

}
