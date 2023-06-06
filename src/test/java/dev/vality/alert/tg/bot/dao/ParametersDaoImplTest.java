package dev.vality.alert.tg.bot.dao;

import dev.vality.alert.tg.bot.TestObjectFactory;
import dev.vality.alert.tg.bot.config.PostgresqlJooqTest;
import dev.vality.alert.tg.bot.dao.impl.ParametersDaoImpl;
import dev.vality.alert.tg.bot.domain.tables.pojos.ParametersData;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static dev.vality.alert.tg.bot.domain.Tables.PARAMETERS_DATA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@PostgresqlJooqTest
@ContextConfiguration(classes = {ParametersDaoImpl.class})
class ParametersDaoImplTest {

    @Autowired
    private ParametersDao parametersDao;

    @Autowired
    private DSLContext dslContext;

    @BeforeEach
    void setUp() {
        dslContext.deleteFrom(PARAMETERS_DATA).execute();
    }

    @Test
    void save() {
        ParametersData parameters = TestObjectFactory.testParameters();

        ParametersData savedParameters = parametersDao.save(parameters);

        assertEquals(1, dslContext.fetchCount(PARAMETERS_DATA));
        assertNotNull(savedParameters);
        assertNotNull(savedParameters.getId());
        assertEquals(parameters.getParamId(), savedParameters.getParamId());
    }

    @Test
    void getByAlertIdAndParamName() {
        ParametersData parameters = TestObjectFactory.testParameters();
        dslContext.insertInto(PARAMETERS_DATA)
                .set(dslContext.newRecord(PARAMETERS_DATA, parameters))
                .execute();

        ParametersData params =
                parametersDao.getByAlertIdAndParamName(parameters.getAlertId(), parameters.getParamName());

        assertEquals(parameters.getParamId(), params.getParamId());
    }

}