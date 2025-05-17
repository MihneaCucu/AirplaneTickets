package DAO;

import Domain.Flight;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static FlightDAO instance;

    private FlightDAO() {
    }

    public static FlightDAO getInstance() {
        if (instance == null) {
            synchronized (FlightDAO.class) {
                if (instance == null) {
                    instance = new FlightDAO();
                }
            }
        }
        return instance;
    }
    private Timestamp toTimestamp(LocalDateTime ldt) {
        return ldt == null ? null : Timestamp.valueOf(ldt);
    }

    private LocalDateTime fromTimestamp(Timestamp ts) {
        return ts == null ? null : ts.toLocalDateTime();
    }

    public int insertFlight(Flight flight) {
        String sql = "INSERT INTO Flights (DepartureAirport, ArrivalAirport, DepartureDateTime, ArrivalDateTime, Aircraft) VALUES (?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, flight.getDepartureAirportId());
            stmt.setInt(2, flight.getArrivalAirportId());
            stmt.setTimestamp(3, toTimestamp(flight.getDepartureDateTime()));
            stmt.setTimestamp(4, toTimestamp(flight.getArrivalDateTime()));
            stmt.setInt(5, flight.getAircraftId());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return generatedId;
    }

    public List<Flight> getAllFlights() {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM Flights";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Flight flight = new Flight(
                        rs.getInt("FlightNumber"),
                        rs.getInt("DepartureAirport"),
                        rs.getInt("ArrivalAirport"),
                        fromTimestamp(rs.getTimestamp("DepartureDateTime")),
                        fromTimestamp(rs.getTimestamp("ArrivalDateTime")),
                        rs.getInt("Aircraft")
                );
                flights.add(flight);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flights;
    }

    public int updateFlight(Flight flight) {
        String sql = "UPDATE Flights SET DepartureAirport = ?, ArrivalAirport = ?, DepartureDateTime = ?, ArrivalDateTime = ?, Aircraft = ? WHERE FlightNumber = ?";
        int returnValue = 0;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, flight.getDepartureAirportId());
            stmt.setInt(2, flight.getArrivalAirportId());
            stmt.setTimestamp(3, toTimestamp(flight.getDepartureDateTime()));
            stmt.setTimestamp(4, toTimestamp(flight.getArrivalDateTime()));
            stmt.setInt(5, flight.getAircraftId());
            stmt.setInt(6, flight.getFlightNumber());

            returnValue = stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return returnValue;
    }

    public int deleteFlight(int flightNumber) {
        String sql = "DELETE FROM Flights WHERE FlightNumber = ?";
        int returnValue = 0;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, flightNumber);
            returnValue = stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return returnValue;
    }

    public List<Flight> getFlightsByAircraftId(int aircraftId) {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM Flights WHERE Aircraft = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, aircraftId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Flight flight = new Flight(
                        rs.getInt("FlightNumber"),
                        rs.getInt("DepartureAirport"),
                        rs.getInt("ArrivalAirport"),
                        fromTimestamp(rs.getTimestamp("DepartureDateTime")),
                        fromTimestamp(rs.getTimestamp("ArrivalDateTime")),
                        rs.getInt("Aircraft")
                );
                flights.add(flight);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flights;
    }
}