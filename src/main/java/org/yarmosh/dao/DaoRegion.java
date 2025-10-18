package org.yarmosh.dao;
import org.yarmosh.db.JDBCConnectionException;
import org.yarmosh.model.CitizenType;
import org.yarmosh.model.Region;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoRegion extends DAO<Region>{

    private static final String CREATE_REGION =
            "INSERT INTO region (name, square, citizen_type) VALUES (?, ?, ?)";
    private static final String READ_REGION =
            "SELECT id, name, square, citizen_type FROM region WHERE id = ?";
    private static final String UPDATE_REGION =
            "UPDATE region SET name = ?, square = ?, citizen_type = ? WHERE id = ?";
    private static final String DELETE_REGION =
            "DELETE FROM region WHERE id = ?";
    private static final String SELECT_ALL_REGION =
            "SELECT * FROM region";

    public void create(Region region) throws JDBCConnectionException {
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(CREATE_REGION)) {

            ps.setString(1, region.getName());
            ps.setString(2, String.valueOf(region.getSquare()));
            ps.setString(3, String.valueOf(region.getCitizenType()));
            ps.executeUpdate();

        } catch (SQLException | JDBCConnectionException e) {
            throw new JDBCConnectionException("Failed to create Region", e);
        }
    }
    public Region read(int id) throws JDBCConnectionException {
        DaoCitizenType daoCitizenType = new DaoCitizenType();
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(READ_REGION)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Region(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("square"),
                        daoCitizenType.read(rs.getInt("citizen_type"))
                );
            }
            return null;

        } catch (SQLException e) {
            throw new JDBCConnectionException("Failed to read Region", e);
        }
    }
    public void update(Region region) throws JDBCConnectionException {
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_REGION)) {

            ps.setString(1, region.getName());
            ps.setInt(2, region.getSquare());
            ps.setInt(3, region.getCitizenType());
            ps.setInt(4, region.getId());
            ps.executeUpdate();

        } catch (SQLException | JDBCConnectionException e) {
            throw new JDBCConnectionException("Failed to update Region", e);
        }
    }
    public void delete(int id) throws JDBCConnectionException {
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_REGION)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException | JDBCConnectionException e) {
            throw new JDBCConnectionException("Failed to delete Region", e);
        }
    }
    public List<Region> getAll() throws JDBCConnectionException {
        DaoCitizenType daoCitizenType = new DaoCitizenType();

        List<Region> regions = new ArrayList<>();

        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_REGION)) {

            ResultSet rs = ps.executeQuery();
            System.out.println("query executed");
            while (rs.next()) {
                Region region = new Region(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("square"),
                        daoCitizenType.read(rs.getInt("citizen_type"))
                );
                regions.add(region);
            }
        } catch (SQLException | JDBCConnectionException e) {
            e.printStackTrace(); // prints full stack trace
            throw new JDBCConnectionException("Failed to get all Regions", e);
        }
        return regions;
    }
}
