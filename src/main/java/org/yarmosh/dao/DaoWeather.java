package org.yarmosh.dao;

import org.yarmosh.db.JDBCConnectionException;
import org.yarmosh.model.Region;
import org.yarmosh.model.Weather;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoWeather extends DAO<Weather> {

    private static final String CREATE_WEATHER =
            "INSERT INTO weather (RegionId, Date, Temperature, Precipitation) VALUES (?, ?, ?, ?)";
    private static final String READ_WEATHER =
            "SELECT * FROM weather WHERE id = ?";
    private static final String UPDATE_WEATHER =
            "UPDATE weather SET RegionId = ?, Date = ?, Temperature = ?, Precipitation = ? WHERE id = ?";
    private static final String DELETE_WEATHER =
            "DELETE FROM weather WHERE id = ?";
    private static final String SELECT_ALL_WEATHER =
            "SELECT * FROM weather";

    private static final Logger logger = Logger.getLogger(DaoWeather.class.getName());

    public DaoWeather() throws JDBCConnectionException {
        super();
    }

    // CREATE
    public void create(Weather weather) throws JDBCConnectionException {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(CREATE_WEATHER);
            ps.setInt(1, weather.getRegion()); // предполагается, что getRegion() возвращает ID региона
            ps.setString(2, weather.getDate());
            ps.setInt(3, weather.getTemperature());
            ps.setString(4, weather.getPrecipitation());

            ps.executeUpdate();
            logger.info("Запись о погоде успешно добавлена: регион " + weather.getRegion());

        } catch (JDBCConnectionException e) {
            logger.log(Level.SEVERE, "Ошибка подключения при добавлении Weather", e);
            throw e;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка SQL при добавлении Weather", e);
            throw new JDBCConnectionException("Не удалось добавить Weather в базу данных", e);
        } finally {
            pool.releaseConnection(conn);
        }
    }

    // READ
    public Weather read(int id) throws JDBCConnectionException {
        Connection conn = null;
        DaoRegion daoRegion = new DaoRegion();

        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(READ_WEATHER);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Region region = daoRegion.read(rs.getInt("RegionId"));
                Weather weather = new Weather(
                        rs.getInt("id"),
                        region,
                        rs.getString("Date"),
                        rs.getInt("Temperature"),
                        rs.getString("Precipitation")
                );
                logger.info("Запись о погоде успешно найдена: ID=" + id);
                return weather;
            } else {
                logger.warning("Запись о погоде с ID=" + id + " не найдена.");
                return null;
            }

        } catch (JDBCConnectionException e) {
            logger.log(Level.SEVERE, "Ошибка подключения при чтении Weather", e);
            throw e;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка SQL при чтении Weather", e);
            throw new JDBCConnectionException("Не удалось прочитать Weather из базы данных", e);
        } finally {
            pool.releaseConnection(conn);
        }
    }

    // UPDATE
    public void update(Weather weather) throws JDBCConnectionException {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE_WEATHER);
            ps.setInt(1, weather.getRegion());
            ps.setString(2, weather.getDate());
            ps.setInt(3, weather.getTemperature());
            ps.setString(4, weather.getPrecipitation());
            ps.setInt(5, weather.getId());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                logger.info("Запись о погоде успешно обновлена: ID=" + weather.getId());
            } else {
                logger.warning("Не удалось обновить запись о погоде: ID=" + weather.getId());
            }

        } catch (JDBCConnectionException e) {
            logger.log(Level.SEVERE, "Ошибка подключения при обновлении Weather", e);
            throw e;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка SQL при обновлении Weather", e);
            throw new JDBCConnectionException("Не удалось обновить Weather в базе данных", e);
        } finally {
            pool.releaseConnection(conn);
        }
    }

    // DELETE
    public void delete(int id) throws JDBCConnectionException {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(DELETE_WEATHER);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                logger.info("Запись о погоде успешно удалена: ID=" + id);
            } else {
                logger.warning("Не удалось удалить запись о погоде: ID=" + id);
            }

        } catch (JDBCConnectionException e) {
            logger.log(Level.SEVERE, "Ошибка подключения при удалении Weather", e);
            throw e;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка SQL при удалении Weather", e);
            throw new JDBCConnectionException("Не удалось удалить Weather из базы данных", e);
        } finally {
            pool.releaseConnection(conn);
        }
    }

    // GET ALL
    public List<Weather> getAll() throws JDBCConnectionException {
        Connection conn = null;
        List<Weather> weathers = new ArrayList<>();
        DaoRegion daoRegion = new DaoRegion();

        try {
            conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_ALL_WEATHER);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Region region = daoRegion.read(rs.getInt("RegionId"));
                weathers.add(new Weather(
                        rs.getInt("id"),
                        region,
                        rs.getString("Date"),
                        rs.getInt("Temperature"),
                        rs.getString("Precipitation")
                ));
            }

            logger.info("Все записи о погоде успешно получены. Количество: " + weathers.size());

        } catch (JDBCConnectionException e) {
            logger.log(Level.SEVERE, "Ошибка подключения при получении списка Weather", e);
            throw e;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка SQL при получении списка Weather", e);
            throw new JDBCConnectionException("Не удалось получить список Weather из базы данных", e);
        } finally {
            pool.releaseConnection(conn);
        }

        return weathers;
    }
}
