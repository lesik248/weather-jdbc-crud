package org.yarmosh.dao;

import org.yarmosh.db.JDBCConnectionException;
import org.yarmosh.model.Region;
import org.yarmosh.model.CitizenType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoRegion extends DAO<Region> {

    private static final String CREATE_REGION =
            "INSERT INTO region (name, square, citizen_type) VALUES (?, ?, ?)";
    private static final String READ_REGION =
            "SELECT * FROM region WHERE id = ?";
    private static final String UPDATE_REGION =
            "UPDATE region SET name = ?, square = ?, citizen_type = ? WHERE id = ?";
    private static final String DELETE_REGION =
            "DELETE FROM region WHERE id = ?";
    private static final String SELECT_ALL_REGION =
            "SELECT * FROM region";

    private static final Logger logger = Logger.getLogger(DaoRegion.class.getName());

    public DaoRegion() throws JDBCConnectionException {
        super();
    }

    // CREATE
    public void create(Region region) throws JDBCConnectionException {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(CREATE_REGION);
            ps.setString(1, region.getName());
            ps.setInt(2, region.getSquare());
            ps.setInt(3, region.getCitizenType()); // предполагается, что это id CitizenType

            ps.executeUpdate();
            logger.info("Регион успешно добавлен: " + region.getName());

        } catch (JDBCConnectionException e) {
            logger.log(Level.SEVERE, "Ошибка подключения при добавлении Region", e);
            throw e;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка SQL при добавлении Region", e);
            throw new JDBCConnectionException("Не удалось добавить Region в базу данных", e);
        } finally {
            pool.releaseConnection(conn);
        }
    }

    // READ
    public Region read(int id) throws JDBCConnectionException {
        Connection conn = null;
        DaoCitizenType daoCitizenType = new DaoCitizenType();

        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(READ_REGION);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                CitizenType citizenType = daoCitizenType.read(rs.getInt("citizen_type"));
                Region region = new Region(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("square"),
                        citizenType
                );
                logger.info("Регион успешно найден: ID=" + id);
                return region;
            } else {
                logger.warning("Регион с ID=" + id + " не найден.");
                return null;
            }

        } catch (JDBCConnectionException e) {
            logger.log(Level.SEVERE, "Ошибка подключения при чтении Region", e);
            throw e;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка SQL при чтении Region", e);
            throw new JDBCConnectionException("Не удалось прочитать Region из базы данных", e);
        } finally {
            pool.releaseConnection(conn);
        }
    }

    // UPDATE
    public void update(Region region) throws JDBCConnectionException {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE_REGION);
            ps.setString(1, region.getName());
            ps.setInt(2, region.getSquare());
            ps.setInt(3, region.getCitizenType());
            ps.setInt(4, region.getId());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                logger.info("Регион успешно обновлён: ID=" + region.getId());
            } else {
                logger.warning("Не удалось обновить регион: ID=" + region.getId());
            }

        } catch (JDBCConnectionException e) {
            logger.log(Level.SEVERE, "Ошибка подключения при обновлении Region", e);
            throw e;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка SQL при обновлении Region", e);
            throw new JDBCConnectionException("Не удалось обновить Region в базе данных", e);
        } finally {
            pool.releaseConnection(conn);
        }
    }

    // DELETE
    public void delete(int id) throws JDBCConnectionException {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(DELETE_REGION);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                logger.info("Регион успешно удалён: ID=" + id);
            } else {
                logger.warning("Не удалось удалить регион: ID=" + id);
            }

        } catch (JDBCConnectionException e) {
            logger.log(Level.SEVERE, "Ошибка подключения при удалении Region", e);
            throw e;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка SQL при удалении Region", e);
            throw new JDBCConnectionException("Не удалось удалить Region из базы данных", e);
        } finally {
            pool.releaseConnection(conn);
        }
    }

    // GET ALL
    public List<Region> getAll() throws JDBCConnectionException {
        Connection conn = null;
        List<Region> regions = new ArrayList<>();
        DaoCitizenType daoCitizenType = new DaoCitizenType();

        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_ALL_REGION);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CitizenType citizenType = daoCitizenType.read(rs.getInt("citizen_type"));
                regions.add(new Region(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("square"),
                        citizenType
                ));
            }

            logger.info("Все регионы успешно получены. Количество: " + regions.size());

        } catch (JDBCConnectionException e) {
            logger.log(Level.SEVERE, "Ошибка подключения при получении списка Region", e);
            throw e;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка SQL при получении списка Region", e);
            throw new JDBCConnectionException("Не удалось получить список Region из базы данных", e);
        } finally {
            pool.releaseConnection(conn);
        }

        return regions;
    }
}
