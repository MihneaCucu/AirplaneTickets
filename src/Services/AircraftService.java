package Services;

import DAO.AircraftDAO;
import Domain.Aircraft;

import java.util.List;

public class AircraftService {
    private static AircraftService instance;
    private final AircraftDAO aircraftDAO;

    private AircraftService() {
        this.aircraftDAO = AircraftDAO.getInstance();
    }

    public static AircraftService getInstance() {
        if (instance == null) {
            synchronized (AircraftService.class) {
                if (instance == null) {
                    instance = new AircraftService();
                }
            }
        }
        return instance;
    }

    public void addAircraft(Aircraft aircraft) {
        aircraftDAO.insertAircraft(aircraft);
        System.out.println("Aircraft added to database: " + aircraft);
        AuditService.getInstance().logAction("Aircraft added: " + aircraft);
    }

    public List<Aircraft> getAllAircraft() {
        return aircraftDAO.getAllAircraft();
    }

    public void updateAircraft(Aircraft aircraft) {
        aircraftDAO.updateAircraft(aircraft);
        System.out.println("Aircraft updated in database: " + aircraft);
        AuditService.getInstance().logAction("Aircraft updated: " + aircraft);
    }

    public void deleteAircraft(int id) {
        aircraftDAO.deleteAircraft(id);
        System.out.println("Aircraft deleted from database with ID: " + id);
        AuditService.getInstance().logAction("Deleted aircraft with ID: " + id);
    }
}