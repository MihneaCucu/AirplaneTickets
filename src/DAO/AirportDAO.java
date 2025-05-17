package DAO;

import Domain.Airport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AirportDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static AirportDAO instance;

    private AirportDAO() {
    }

    public static AirportDAO getInstance() {
        if (instance == null) {
            synchronized (AirportDAO.class) {
                if (instance == null) {
                    instance = new AirportDAO();
                }
            }
        }
        return instance;
    }

    public void insertAirport(Airport airport) {
        String sql = "INSERT INTO Airports (Name, City, Country) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, airport.getName());
            stmt.setString(2, airport.getCity());
            stmt.setString(3, airport.getCountry());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Airport> getAllAirports() {
        List<Airport> airports = new ArrayList<>();
        String sql = "SELECT * FROM Airports";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Airport airport = new Airport(
                        rs.getInt("ID"),
                        rs.getString("Name"),
                        rs.getString("City"),
                        rs.getString("Country")
                );
                airports.add(airport);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return airports;
    }

    public List<Airport> getAirportsByCity(String city) {
        List<Airport> airports = new ArrayList<>();
        String sql = "SELECT * FROM Airports WHERE City = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, city);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Airport airport = new Airport(
                        rs.getInt("ID"),
                        rs.getString("Name"),
                        rs.getString("City"),
                        rs.getString("Country")
                );
                airports.add(airport);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return airports;
    }

    public int updateAirport(int id, String name) {
        String sql = "UPDATE Airports SET Name = ? WHERE ID = ?";
        int returnValue = 0;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setInt(2, id);
            returnValue = stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return returnValue;
    }

    public int deleteAirport(int id) {
        String sql = "DELETE FROM Airports WHERE ID = ?";
        int returnValue = 0;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            returnValue = stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return returnValue;
    }
}