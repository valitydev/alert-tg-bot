package dev.vality.alert.tg.bot.dao;

import dev.vality.alert.tg.bot.TestObjectFactory;
import dev.vality.alert.tg.bot.config.PostgresqlJooqTest;
import dev.vality.alert.tg.bot.dao.impl.StateDataDaoImpl;
import dev.vality.alert.tg.bot.domain.tables.pojos.StateData;
import dev.vality.alert.tg.bot.domain.tables.records.StateDataRecord;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static dev.vality.alert.tg.bot.domain.Tables.STATE_DATA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@PostgresqlJooqTest
@ContextConfiguration(classes = {StateDataDaoImpl.class})
class StateDaoImplTest {

    @Autowired
    private StateDataDao stateDataDao;

    @Autowired
    private DSLContext dslContext;

    @BeforeEach
    void setUp() {
        dslContext.deleteFrom(STATE_DATA).execute();
    }

    @Test
    void save() {
        StateData stateData = TestObjectFactory.testStateData();

        StateData savedStateData = stateDataDao.save(stateData);

        assertEquals(1, dslContext.fetchCount(STATE_DATA));
        assertNotNull(savedStateData);
        assertNotNull(savedStateData.getId());
        assertEquals(stateData.getAlertId(), savedStateData.getAlertId());
    }

    @Test
    void updateParams() {
        StateData stateData = TestObjectFactory.testStateData();
        stateData.setMapParams("{\"Процент\":56,\"Имя Терминала\":test}");
        dslContext.insertInto(STATE_DATA)
                .set(dslContext.newRecord(STATE_DATA, stateData))
                .execute();

        String newMapParams = "{\"Процент\":56,\"Имя Терминала\":new}";
        stateDataDao.updateParams(stateData.getUserId(), newMapParams);

        StateDataRecord stateDataRecord = dslContext.fetchAny(STATE_DATA);

        assertEquals(newMapParams, stateDataRecord.getMapParams());
    }

    @Test
    void getByUserId() {
        StateData stateData = TestObjectFactory.testStateData();
        dslContext.insertInto(STATE_DATA)
                .set(dslContext.newRecord(STATE_DATA, stateData))
                .execute();

        StateData data = stateDataDao.getByUserId(stateData.getUserId());

        assertEquals(stateData.getUserId(), data.getUserId());
    }

}
