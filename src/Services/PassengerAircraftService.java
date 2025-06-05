package Services;

import DAO.PassengerAircraftDAO;
import Domain.PassengerAircraft;
import java.util.List;

public class PassengerAircraftService {
    private static PassengerAircraftService instance;
    private final PassengerAircraftDAO passengerAircraftDAO;

    private PassengerAircraftService() {
        this.passengerAircraftDAO = PassengerAircraftDAO.getInstance();
    }

    public static PassengerAircraftService getInstance() {
        if (instance == null) {
            synchronized (PassengerAircraftService.class) {
                if (instance == null) {
                    instance = new PassengerAircraftService();
                }
            }
        }
        return instance;
    }

    public void addPassengerAircraft(PassengerAircraft aircraft) {
        passengerAircraftDAO.add(aircraft);
        System.out.println("Passenger aircraft added to database: " + aircraft);
        AuditService.getInstance().logAction("Passenger aircraft added: " + aircraft);
    }

    public List<PassengerAircraft> getAllPassengerAircraft() {
        return passengerAircraftDAO.loadAll();
    }

    public void updatePassengerAircraft(PassengerAircraft aircraft) {
        String sql = "UPDATE PassengerAircraft SET TotalSeats = ? WHERE ID = ?";
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "");
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, aircraft.getTotalSeats());
            stmt.setInt(2, aircraft.getId());
            stmt.executeUpdate();
            System.out.println("Passenger aircraft updated in database: " + aircraft);
            AuditService.getInstance().logAction("Passenger aircraft updated: " + aircraft);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePassengerAircraft(int id) {
        passengerAircraftDAO.remove(id);
        System.out.println("Passenger aircraft deleted from database with ID: " + id);
        AuditService.getInstance().logAction("Deleted passenger aircraft with ID: " + id);
    }
}
