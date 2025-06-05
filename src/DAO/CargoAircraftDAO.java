package DAO;

import Domain.CargoAircraft;
import Utils.AircraftType;
import java.sql.*;
import java.util.*;

public class CargoAircraftDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static CargoAircraftDAO instance;
    private final GenericDAO<CargoAircraft> genericDAO;

    private CargoAircraftDAO() {
        genericDAO = GenericDAO.getInstance();
    }

    public static CargoAircraftDAO getInstance() {
        if (instance == null) {
            synchronized (CargoAircraftDAO.class) {
                if (instance == null) {
                    instance = new CargoAircraftDAO();
                }
            }
        }
        return instance;
    }

    public void insertCargoAircraft(CargoAircraft aircraft) {
        String insertAircraftSql = "INSERT INTO Aircraft (AircraftType) VALUES (?)";
        String insertCargoSql = "INSERT INTO CargoAircraft (ID, CargoCapacity) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmtAircraft = conn.prepareStatement(insertAircraftSql, Statement.RETURN_GENERATED_KEYS)) {
            stmtAircraft.setString(1, aircraft.getAircraftType().toString());
            stmtAircraft.executeUpdate();
            try (ResultSet generatedKeys = stmtAircraft.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    try (PreparedStatement stmtCargo = conn.prepareStatement(insertCargoSql)) {
                        stmtCargo.setInt(1, id);
                        stmtCargo.setInt(2, aircraft.getCargoCapacity());
                        stmtCargo.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CargoAircraft> loadAll() {
        List<CargoAircraft> aircraftList = new ArrayList<>();
        String sql = "SELECT a.ID, a.AircraftType, c.CargoCapacity FROM Aircraft a JOIN CargoAircraft c ON a.ID = c.ID";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("ID");
                AircraftType type = AircraftType.valueOf(rs.getString("AircraftType"));
                int cargoCapacity = rs.getInt("CargoCapacity");
                aircraftList.add(new CargoAircraft(id, type, cargoCapacity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aircraftList;
    }

    public void saveAll(List<CargoAircraft> aircraftList) {
        // Assuming you want to overwrite the database table with the list content
        String sql = "DELETE FROM CargoAircraft";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            for (CargoAircraft aircraft : aircraftList) {
                insertCargoAircraft(aircraft);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(CargoAircraft aircraft) {
        insertCargoAircraft(aircraft);
    }

    public void remove(int id) {
        String sql = "DELETE FROM CargoAircraft WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CargoAircraft findById(int id) {
        CargoAircraft aircraft = null;
        String sql = "SELECT a.ID, a.AircraftType, c.CargoCapacity FROM Aircraft a JOIN CargoAircraft c ON a.ID = c.ID WHERE a.ID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    AircraftType type = AircraftType.valueOf(rs.getString("AircraftType"));
                    int cargoCapacity = rs.getInt("CargoCapacity");
                    aircraft = new CargoAircraft(id, type, cargoCapacity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aircraft;
    }
}
