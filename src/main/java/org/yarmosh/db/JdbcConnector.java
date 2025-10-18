package org.yarmosh.db;

import org.yarmosh.config.ConfigurationManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnector {
    private Connection conn;

    public Connection getConnection() throws JDBCConnectionException {
        ConfigurationManager cfg = ConfigurationManager.getInstance();
        try {
            Class.forName(cfg.getDriverName());
            conn = DriverManager.getConnection(cfg.getURL(), cfg.getUsername(),
                    cfg.getPassword());
        } catch (ClassNotFoundException e) {
            throw new JDBCConnectionException("Can't load database driver.", e);
        } catch (SQLException e) {
            throw new JDBCConnectionException("Can't connect to database.", e);
        }
        if(conn==null) {
            throw new JDBCConnectionException("Driver type is not correct in URL " +
                    cfg.getURL() + ".");
        }
        return conn;
    }

    public void close() throws JDBCConnectionException {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new JDBCConnectionException("Can't close connection", e);
            }
        }
    }
}
