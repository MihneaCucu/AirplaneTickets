package DAO;

import Domain.Seat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static SeatDAO instance;
    private final GenericDAO<Seat> genericDAO;

    private SeatDAO() {
        genericDAO = GenericDAO.getInstance();
    }

    public static SeatDAO getInstance() {
        if (instance == null) {
            synchronized (SeatDAO.class) {
                if (instance == null) {
                    instance = new SeatDAO();
                }
            }
        }
        return instance;
    }

    public void insertSeat(Seat seat) {
        String sql = "INSERT INTO Seats (`Row`, Position, ExtraLegRoom, PassengerName, FlightID) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, seat.getRow());
            stmt.setString(2, String.valueOf(seat.getPosition()));
            stmt.setBoolean(3, seat.isExtraLegRoom());
            stmt.setString(4, seat.getPassengerName());
            stmt.setInt(5, seat.getFlightId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Seat> getAllSeats() {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM Seats";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Seat seat = new Seat(
                        rs.getInt("ID"),
                        rs.getInt("Row"),
                        rs.getString("Position").charAt(0),
                        rs.getBoolean("ExtraLegRoom"),
                        rs.getString("PassengerName"),
                        rs.getInt("FlightID")
                );
                seats.add(seat);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seats;
    }

    public int updateSeat(Seat seat) {
        String sql = "UPDATE Seats SET PassengerName = ? WHERE FlightID = ? AND `Row` = ? AND `Position` = ? AND PassengerName IS NULL";
        int returnValue = 0;
        try {
            returnValue = genericDAO.executeUpdate(sql, seat.getPassengerName(), seat.getFlightId(), seat.getRow(), String.valueOf(seat.getPosition()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    public int deleteSeat(int flightID, String passengerName) {
        String sql = "UPDATE Seats SET PassengerName = NULL WHERE FlightID = ? AND PassengerName = ?";
        int returnValue = 0;
        try {
            returnValue = genericDAO.executeUpdate(sql, flightID, passengerName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    public void deleteSeatsByFlightId(int flightId) {
        String sql = "DELETE FROM Seats WHERE FlightID = ?";
        try {
            genericDAO.executeUpdate(sql, flightId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}