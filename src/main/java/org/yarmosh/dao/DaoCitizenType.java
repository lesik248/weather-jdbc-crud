package org.yarmosh.dao;

import org.yarmosh.db.JDBCConnectionException;
import org.yarmosh.model.CitizenType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoCitizenType extends DAO<CitizenType> {

    private static final String CREATE_CITIZEN_TYPE =
            "INSERT INTO citizen_type (name, language, number) VALUES (?, ?, ?)";
    private static final String READ_CITIZEN_TYPE =
            "SELECT * FROM citizen_type WHERE id = ?";
    private static final String UPDATE_CITIZEN_TYPE =
            "UPDATE citizen_type SET name = ?, language = ?, number = ? WHERE id = ?";
    private static final String DELETE_CITIZEN_TYPE =
            "DELETE FROM citizen_type WHERE id = ?";
    private static final String SELECT_ALL_CITIZEN_TYPE =
            "SELECT * FROM citizen_type";

    private static final Logger logger = Logger.getLogger(DaoCitizenType.class.getName());

    public DaoCitizenType() throws JDBCConnectionException {
        super();
    }

    // CREATE
    public void create(CitizenType type) throws JDBCConnectionException {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(CREATE_CITIZEN_TYPE);
            ps.setString(1, type.getName());
            ps.setString(2, type.getLanguage());
            ps.setInt(3, type.getNumber());

            ps.executeUpdate();
            logger.info("Тип гражданина успешно добавлен: " + type.getName());

        } catch (JDBCConnectionException e) {
            logger.log(Level.SEVERE, "Ошибка подключения при добавлении CitizenType", e);
            throw e;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка SQL при добавлении CitizenType", e);
            throw new JDBCConnectionException("Не удалось добавить CitizenType в базу данных", e);
        } finally {
            pool.releaseConnection(conn);
        }
    }

    // READ
    public CitizenType read(int id) throws JDBCConnectionException {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(READ_CITIZEN_TYPE);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                CitizenType type = new CitizenType(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("language"),
                        rs.getInt("number")
                );
                logger.info("CitizenType успешно найден: ID=" + id);
                return type;
            } else {
                logger.warning("CitizenType с ID=" + id + " не найден.");
                return null;
            }

        } catch (JDBCConnectionException e) {
            logger.log(Level.SEVERE, "Ошибка подключения при чтении CitizenType", e);
            throw e;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка SQL при чтении CitizenType", e);
            throw new JDBCConnectionException("Не удалось прочитать CitizenType из базы данных", e);
        } finally {
            pool.releaseConnection(conn);
        }
    }

    // UPDATE
    public void update(CitizenType type) throws JDBCConnectionException {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE_CITIZEN_TYPE);
            ps.setString(1, type.getName());
            ps.setString(2, type.getLanguage());
            ps.setInt(3, type.getNumber());
            ps.setInt(4, type.getId());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                logger.info("CitizenType успешно обновлён: ID=" + type.getId());
            } else {
                logger.warning("Не удалось обновить CitizenType: ID=" + type.getId());
            }

        } catch (JDBCConnectionException e) {
            logger.log(Level.SEVERE, "Ошибка подключения при обновлении CitizenType", e);
            throw e;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка SQL при обновлении CitizenType", e);
            throw new JDBCConnectionException("Не удалось обновить CitizenType в базе данных", e);
        } finally {
            pool.releaseConnection(conn);
        }
    }

    // DELETE
    public void delete(int id) throws JDBCConnectionException {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(DELETE_CITIZEN_TYPE);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                logger.info("CitizenType успешно удалён: ID=" + id);
            } else {
                logger.warning("Не удалось удалить CitizenType: ID=" + id);
            }

        } catch (JDBCConnectionException e) {
            logger.log(Level.SEVERE, "Ошибка подключения при удалении CitizenType", e);
            throw e;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка SQL при удалении CitizenType", e);
            throw new JDBCConnectionException("Не удалось удалить CitizenType из базы данных", e);
        } finally {
            pool.releaseConnection(conn);
        }
    }

    // GET ALL
    public List<CitizenType> getAll() throws JDBCConnectionException {
        Connection conn = null;
        List<CitizenType> citizenTypes = new ArrayList<>();

        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_ALL_CITIZEN_TYPE);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                citizenTypes.add(new CitizenType(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("language"),
                        rs.getInt("number")
                ));
            }

            logger.info("Все CitizenType успешно получены. Количество: " + citizenTypes.size());

        } catch (JDBCConnectionException e) {
            logger.log(Level.SEVERE, "Ошибка подключения при получении списка CitizenType", e);
            throw e;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка SQL при получении списка CitizenType", e);
            throw new JDBCConnectionException("Не удалось получить список CitizenType из базы данных", e);
        } finally {
            pool.releaseConnection(conn);
        }

        return citizenTypes;
    }
}
