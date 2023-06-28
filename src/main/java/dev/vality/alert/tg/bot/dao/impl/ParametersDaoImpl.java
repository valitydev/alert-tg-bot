package dev.vality.alert.tg.bot.dao.impl;


import dev.vality.alert.tg.bot.dao.AbstractDao;
import dev.vality.alert.tg.bot.dao.ParametersDao;
import dev.vality.alert.tg.bot.domain.tables.pojos.ParametersData;
import dev.vality.mapper.RecordRowMapper;
import org.jooq.Query;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;

import static dev.vality.alert.tg.bot.domain.Tables.PARAMETERS_DATA;


@Component
public class ParametersDaoImpl extends AbstractDao implements ParametersDao {

    private final RecordRowMapper<ParametersData> parameterTypeRecordRowMapper;

    public ParametersDaoImpl(DataSource dataSource) {
        super(dataSource);
        parameterTypeRecordRowMapper = new RecordRowMapper<>(PARAMETERS_DATA, ParametersData.class);
    }

    @Override
    public ParametersData save(ParametersData parametersData) {
        Query query = getDslContext()
                .insertInto(PARAMETERS_DATA)
                .set(getDslContext().newRecord(PARAMETERS_DATA, parametersData))
                .onConflict(PARAMETERS_DATA.PARAM_ID)
                .doUpdate()
                .set(getDslContext().newRecord(PARAMETERS_DATA, parametersData))
                .returning(PARAMETERS_DATA.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        Long id = Optional.ofNullable(keyHolder.getKey())
                .map(Number::longValue)
                .orElseThrow(() -> new IllegalStateException("Can't get data id"));
        parametersData.setId(id);
        return parametersData;
    }


    @Override
    public ParametersData getByAlertIdAndParamName(String alertId, String paramName) {
        var get = getDslContext()
                .selectFrom(PARAMETERS_DATA)
                .where(PARAMETERS_DATA.ALERT_ID.eq(alertId))
                .and(PARAMETERS_DATA.PARAM_NAME.eq(paramName));
        return fetchOne(get, parameterTypeRecordRowMapper);
    }

    @Override
    public ParametersData getByAlertIdAndParamId(String alertId, String paramId) {
        var get = getDslContext()
                .selectFrom(PARAMETERS_DATA)
                .where(PARAMETERS_DATA.ALERT_ID.eq(alertId))
                .and(PARAMETERS_DATA.PARAM_ID.eq(paramId));
        return fetchOne(get, parameterTypeRecordRowMapper);
    }
}
