package dev.vality.alert.tg.bot.dao.impl;


import dev.vality.alert.tg.bot.dao.AbstractDao;
import dev.vality.alert.tg.bot.dao.StateDataDao;
import dev.vality.alert.tg.bot.domain.tables.pojos.StateData;
import dev.vality.alert.tg.bot.domain.tables.records.StateDataRecord;
import dev.vality.mapper.RecordRowMapper;
import org.jooq.Query;
import org.jooq.UpdateConditionStep;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;

import static dev.vality.alert.tg.bot.domain.Tables.STATE_DATA;


@Component
public class StateDataDaoImpl extends AbstractDao implements StateDataDao {

    private final RecordRowMapper<StateData> stateDataRecordRowMapper;

    public StateDataDaoImpl(DataSource dataSource) {
        super(dataSource);
        stateDataRecordRowMapper = new RecordRowMapper<>(STATE_DATA, StateData.class);
    }

    @Override
    public StateData save(StateData data) {
        Query query = getDslContext()
                .insertInto(STATE_DATA)
                .set(getDslContext().newRecord(STATE_DATA, data))
                .onConflict(STATE_DATA.USER_ID)
                .doUpdate()
                .set(getDslContext().newRecord(STATE_DATA, data))
                .returning(STATE_DATA.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        Long id = Optional.ofNullable(keyHolder.getKey())
                .map(Number::longValue)
                .orElseThrow(() -> new IllegalStateException("Can't get data id"));
        data.setId(id);
        return data;
    }

    @Override
    public void updateParams(Long userId, String params) {
        UpdateConditionStep<StateDataRecord> update = getDslContext()
                .update(STATE_DATA)
                .set(STATE_DATA.MAP_PARAMS, params)
                .where(STATE_DATA.USER_ID.eq(userId));
        execute(update);
    }

    @Override
    public StateData getByUserId(Long userId) {
        var get = getDslContext()
                .selectFrom(STATE_DATA)
                .where(STATE_DATA.USER_ID.eq(userId));
        return fetchOne(get, stateDataRecordRowMapper);
    }
}
