package dev.vality.alert.tg.bot.dao.impl;


import dev.vality.alert.tg.bot.dao.AbstractDao;
import dev.vality.alert.tg.bot.dao.ParametersDao;
import dev.vality.alert.tg.bot.domain.tables.pojos.Parameters;
import dev.vality.mapper.RecordRowMapper;
import org.jooq.Query;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;

import static dev.vality.alert.tg.bot.domain.Tables.PARAMETERS;


@Component
public class ParametersDaoImpl extends AbstractDao implements ParametersDao {

    private final RecordRowMapper<Parameters> parameterTypeRecordRowMapper;

    public ParametersDaoImpl(DataSource dataSource) {
        super(dataSource);
        parameterTypeRecordRowMapper = new RecordRowMapper<>(PARAMETERS, Parameters.class);
    }

    @Override
    public Parameters save(Parameters param) {
        Query query = getDslContext()
                .insertInto(PARAMETERS)
                .set(getDslContext().newRecord(PARAMETERS, param))
                .onConflict(PARAMETERS.PARAM_ID)
                .doUpdate()
                .set(getDslContext().newRecord(PARAMETERS, param))
                .returning(PARAMETERS.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        Long id = Optional.ofNullable(keyHolder.getKey())
                .map(Number::longValue)
                .orElseThrow(() -> new IllegalStateException("Can't get data id"));
        param.setId(id);
        return param;
    }


    @Override
    public Parameters getByAlertIdAndParamName(String alertId, String paramName) {
        var get = getDslContext()
                .selectFrom(PARAMETERS)
                .where(PARAMETERS.ALERT_ID.eq(alertId))
                .and(PARAMETERS.PARAM_NAME.eq(paramName));
        return fetchOne(get, parameterTypeRecordRowMapper);
    }
}
