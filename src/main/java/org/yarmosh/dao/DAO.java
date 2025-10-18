package org.yarmosh.dao;
import java.sql.SQLException;
import java.util.List;

import org.yarmosh.db.JDBCConnectionException;
import org.yarmosh.db.JdbcConnector;

public abstract class DAO<T> {
    protected final JdbcConnector connector = new JdbcConnector();

    public JdbcConnector getJdbcConnector() {
        return connector;
    }
    public abstract void create(T item) throws SQLException, JDBCConnectionException;
    public abstract T read(int id) throws SQLException, JDBCConnectionException;
    public abstract void update(T entity) throws SQLException, JDBCConnectionException;
    public abstract void delete(int id) throws SQLException, JDBCConnectionException;
    public abstract List<T> getAll() throws SQLException, JDBCConnectionException;
}
