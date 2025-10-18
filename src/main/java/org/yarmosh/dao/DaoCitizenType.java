package org.yarmosh.dao;

import org.yarmosh.db.JDBCConnectionException;
import org.yarmosh.model.CitizenType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoCitizenType extends DAO<CitizenType> {

    private static final String CREATE_CITIZEN_TYPE =
            "INSERT INTO citizen_type (name, language, number) VALUES (?, ?, ?)";
    private static final String READ_CITIZEN_TYPE =
            "SELECT id, name, language, number FROM citizen_type WHERE id = ?";
    private static final String UPDATE_CITIZEN_TYPE =
            "UPDATE citizen_type SET name = ?, language = ?, number = ? WHERE id = ?";
    private static final String DELETE_CITIZEN_TYPE =
            "DELETE FROM citizen_type WHERE id = ?";
    private static final String SELECT_ALL_CITIZEN_TYPE =
            "SELECT * FROM citizen_type";

    public DaoCitizenType() {
        this.connector = getJdbcConnector();
    }

    public void create(CitizenType type) throws JDBCConnectionException {

        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(CREATE_CITIZEN_TYPE)) {

            ps.setString(1, type.getName());
            ps.setString(2, type.getLanguage());
            ps.setString(3, String.valueOf(type.getNumber()));
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new JDBCConnectionException("Failed to create CitizenType", e);
        }
    }
    public CitizenType read(int id) throws JDBCConnectionException {
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(READ_CITIZEN_TYPE)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CitizenType(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("language"),
                        rs.getInt("number")
                );
            }
            return null;

        } catch (SQLException e) {
            throw new JDBCConnectionException("Failed to read CitizenType", e);
        }
    }
    public void update(CitizenType type) throws JDBCConnectionException {
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_CITIZEN_TYPE)) {

            ps.setString(1, type.getName());
            ps.setString(2, type.getLanguage());
            ps.setString(3, String.valueOf(type.getNumber()));
            ps.setInt(4, type.getId());
            ps.executeUpdate();

        } catch (SQLException | JDBCConnectionException e) {
            throw new JDBCConnectionException("Failed to update CitizenType", e);
        }
    }
    public void delete(int id) throws JDBCConnectionException {
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_CITIZEN_TYPE)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException | JDBCConnectionException e) {
            throw new JDBCConnectionException("Failed to delete CitizenType", e);
        }
    }
    public List<CitizenType> getAll() throws JDBCConnectionException {
        List<CitizenType> citizenTypes = new ArrayList<>();

        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_CITIZEN_TYPE)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CitizenType citizenType = new CitizenType(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("language"),
                        rs.getInt("number")
                );
                citizenTypes.add(citizenType);
            }
        } catch (SQLException | JDBCConnectionException e) {
            throw new JDBCConnectionException("Failed to delete CitizenType", e);
        }
        return citizenTypes;
    }
}
