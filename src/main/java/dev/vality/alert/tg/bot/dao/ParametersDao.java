package dev.vality.alert.tg.bot.dao;

import dev.vality.alert.tg.bot.domain.tables.pojos.ParametersData;

public interface ParametersDao {

    ParametersData save(ParametersData parametersData);

    ParametersData getByAlertIdAndParamName(String alertId, String paramName);

}
