package Services;

import DAO.CargoAircraftDAO;
import Domain.CargoAircraft;
import java.util.List;

public class CargoAircraftService {
    private static CargoAircraftService instance;
    private final CargoAircraftDAO cargoAircraftDAO;

    private CargoAircraftService() {
        this.cargoAircraftDAO = CargoAircraftDAO.getInstance();
    }

    public static CargoAircraftService getInstance() {
        if (instance == null) {
            synchronized (CargoAircraftService.class) {
                if (instance == null) {
                    instance = new CargoAircraftService();
                }
            }
        }
        return instance;
    }

    public void addCargoAircraft(CargoAircraft aircraft) {
        cargoAircraftDAO.add(aircraft);
        System.out.println("Cargo aircraft added to database: " + aircraft);
        AuditService.getInstance().logAction("Cargo aircraft added: " + aircraft);
    }

    public List<CargoAircraft> getAllCargoAircraft() {
        return cargoAircraftDAO.loadAll();
    }

    public void updateCargoAircraft(CargoAircraft aircraft) {
        String sql = "UPDATE CargoAircraft SET CargoCapacity = ? WHERE ID = ?";
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "");
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, aircraft.getCargoCapacity());
            stmt.setInt(2, aircraft.getId());
            stmt.executeUpdate();
            System.out.println("Cargo aircraft updated in database: " + aircraft);
            AuditService.getInstance().logAction("Cargo aircraft updated: " + aircraft);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCargoAircraft(int id) {
        cargoAircraftDAO.remove(id);
        System.out.println("Cargo aircraft deleted from database with ID: " + id);
        AuditService.getInstance().logAction("Deleted cargo aircraft with ID: " + id);
    }
}
