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

public class DaoWeather extends DAO<Weather> {

    private static final String CREATE_WEATHER =
            "INSERT INTO weather (RegionId, Date, Temperature, Precipitation) VALUES (?, ?, ?, ?)";
    private static final String READ_WEATHER =
            "SELECT id, RegionId, Date, Temperature, Precipitation FROM weather WHERE id = ?";
    private static final String UPDATE_WEATHER =
            "UPDATE weather SET RegionId = ?, Date = ?, Temperature = ?, Precipitation = ? WHERE id = ?";
    private static final String DELETE_WEATHER =
            "DELETE FROM weather WHERE id = ?";
    private static final String SELECT_ALL_WEATHER =
            "SELECT * FROM weather";

    public void create(Weather weather) throws JDBCConnectionException {
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(CREATE_WEATHER)) {

            ps.setInt(1, weather.getRegion());
            ps.setString(2, String.valueOf(weather.getDate()));
            ps.setInt(3, weather.getTemperature());
            ps.setString(4, weather.getPrecipitation());
            ps.executeUpdate();

        } catch (SQLException | JDBCConnectionException e) {
            throw new JDBCConnectionException("Failed to create Weather", e);
        }
    }
    public Weather read(int id) throws JDBCConnectionException {
        DaoRegion daoRegion = new DaoRegion();
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(READ_WEATHER)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Weather(
                        rs.getInt("id"),
                        daoRegion.read(rs.getInt("RegionId")),
                        rs.getString("Date"),
                        rs.getInt("Temperature"),
                        rs.getString("Precipitation")
                );
            }
            return null;

        } catch (SQLException e) {
            throw new JDBCConnectionException("Failed to read Weather", e);
        }
    }
    public void update(Weather weather) throws JDBCConnectionException {
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_WEATHER)) {

            ps.setInt(1, weather.getRegion());
            ps.setString(2, weather.getDate());
            ps.setInt(3, weather.getTemperature());
            ps.setString(4, weather.getPrecipitation());
            ps.setInt(4, weather.getId());
            ps.executeUpdate();

        } catch (SQLException | JDBCConnectionException e) {
            throw new JDBCConnectionException("Failed to update Weather", e);
        }
    }
    public void delete(int id) throws JDBCConnectionException {
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_WEATHER)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException | JDBCConnectionException e) {
            throw new JDBCConnectionException("Failed to delete Weather", e);
        }
    }
    public List<Weather> getAll() throws JDBCConnectionException {
        DaoRegion daoRegion = new DaoRegion();

        List<Weather> weathers = new ArrayList<>();

        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_WEATHER)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Region region = daoRegion.read(rs.getInt("RegionId"));
                Weather weather = new Weather(
                        rs.getInt("id"),
                        region,
                        rs.getString("Date"),
                        rs.getInt("Temperature"),
                        rs.getString("Precipitation")
                );
                weathers.add(weather);
            }
        } catch (SQLException | JDBCConnectionException e) {
            e.printStackTrace(); // ← выведет "Failed to read Region"
            throw new JDBCConnectionException("Failed to get all weathers", e);
        }
        return weathers;
    }
}
