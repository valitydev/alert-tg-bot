package dev.vality.alert.tg.bot.dao;

import dev.vality.alert.tg.bot.domain.tables.pojos.StateData;

public interface StateDataDao {

    StateData save(StateData data);

    void updateParams(Long userId, String params);

    StateData getByUserId(Long userId);

}
