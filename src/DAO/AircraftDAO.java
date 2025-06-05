package DAO;

import Domain.Aircraft;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Utils.AircraftType;

public class AircraftDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static AircraftDAO instance;
    private final GenericDAO<Aircraft> genericDAO;

    private AircraftDAO() {
        genericDAO = GenericDAO.getInstance();
    }

    public static AircraftDAO getInstance() {
        if (instance == null) {
            synchronized (AircraftDAO.class) {
                if (instance == null) {
                    instance = new AircraftDAO();
                }
            }
        }
        return instance;
    }

    public void insertAircraft(Aircraft aircraft) {
        String sql = "INSERT INTO Aircraft (AircraftType) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, aircraft.getAircraftType().toString());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Aircraft> getAllAircraft() {
        List<Aircraft> aircraftList = new ArrayList<>();
        String sql = "SELECT * FROM Aircraft";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Aircraft aircraft = new Aircraft(
                        rs.getInt("ID"),
                        AircraftType.valueOf(rs.getString("AircraftType"))
                );
                aircraftList.add(aircraft);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return aircraftList;
    }

    public void updateAircraft(Aircraft aircraft) {
        String sql = "UPDATE Aircraft SET AircraftType = ? WHERE ID = ?";
        try {
            genericDAO.executeUpdate(sql, aircraft.getAircraftType().toString(), aircraft.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAircraft(int id) {
        String sql = "DELETE FROM Aircraft WHERE ID = ?";
        try {
            genericDAO.executeUpdate(sql, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}