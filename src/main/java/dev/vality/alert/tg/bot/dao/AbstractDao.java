package dev.vality.alert.tg.bot.dao;

import dev.vality.dao.impl.AbstractGenericDao;

import javax.sql.DataSource;

public class AbstractDao extends AbstractGenericDao {

    public AbstractDao(DataSource dataSource) {
        super(dataSource);
    }
}
