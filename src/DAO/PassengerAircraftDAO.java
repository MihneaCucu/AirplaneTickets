package DAO;

import Domain.PassengerAircraft;
import Utils.AircraftType;
import java.sql.*;
import java.util.*;

public class PassengerAircraftDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static PassengerAircraftDAO instance;
    private final GenericDAO<PassengerAircraft> genericDAO;

    private PassengerAircraftDAO() {
        genericDAO = GenericDAO.getInstance();
    }

    public static PassengerAircraftDAO getInstance() {
        if (instance == null) {
            synchronized (PassengerAircraftDAO.class) {
                if (instance == null) {
                    instance = new PassengerAircraftDAO();
                }
            }
        }
        return instance;
    }

    public void insertPassengerAircraft(PassengerAircraft aircraft) {
        String insertAircraftSql = "INSERT INTO Aircraft (AircraftType) VALUES (?)";
        String insertPassengerSql = "INSERT INTO PassengerAircraft (ID, TotalSeats) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmtAircraft = conn.prepareStatement(insertAircraftSql, Statement.RETURN_GENERATED_KEYS)) {
            stmtAircraft.setString(1, aircraft.getAircraftType().toString());
            stmtAircraft.executeUpdate();
            try (ResultSet generatedKeys = stmtAircraft.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    try (PreparedStatement stmtPassenger = conn.prepareStatement(insertPassengerSql)) {
                        stmtPassenger.setInt(1, id);
                        stmtPassenger.setInt(2, aircraft.getTotalSeats());
                        stmtPassenger.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<PassengerAircraft> loadAll() {
        List<PassengerAircraft> aircraftList = new ArrayList<>();
        String sql = "SELECT a.ID, a.AircraftType, p.TotalSeats FROM Aircraft a JOIN PassengerAircraft p ON a.ID = p.ID";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("ID");
                AircraftType type = AircraftType.valueOf(rs.getString("AircraftType"));
                int totalSeats = rs.getInt("TotalSeats");
                aircraftList.add(new PassengerAircraft(id, type, totalSeats));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aircraftList;
    }

    public void saveAll(List<PassengerAircraft> aircraftList) {
        String sql = "DELETE FROM PassengerAircraft";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            for (PassengerAircraft aircraft : aircraftList) {
                insertPassengerAircraft(aircraft);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(PassengerAircraft aircraft) {
        insertPassengerAircraft(aircraft);
    }

    public void remove(int id) {
        // Șterge întâi din PassengerAircraft, apoi din Aircraft
        deletePassengerAircraft(id);
    }

    public void deletePassengerAircraft(int id) {
        String deletePassengerSql = "DELETE FROM PassengerAircraft WHERE ID = ?";
        String deleteAircraftSql = "DELETE FROM Aircraft WHERE ID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmtPassenger = conn.prepareStatement(deletePassengerSql);
             PreparedStatement stmtAircraft = conn.prepareStatement(deleteAircraftSql)) {
            stmtPassenger.setInt(1, id);
            stmtPassenger.executeUpdate();
            stmtAircraft.setInt(1, id);
            stmtAircraft.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PassengerAircraft findById(int id) {
        PassengerAircraft aircraft = null;
        String sql = "SELECT a.ID, a.AircraftType, p.TotalSeats FROM Aircraft a JOIN PassengerAircraft p ON a.ID = p.ID WHERE a.ID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    AircraftType type = AircraftType.valueOf(rs.getString("AircraftType"));
                    int totalSeats = rs.getInt("TotalSeats");
                    aircraft = new PassengerAircraft(id, type, totalSeats);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aircraft;
    }
}
