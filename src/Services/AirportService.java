package Services;

import DAO.AirportDAO;
import Domain.Airport;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AirportService {
    private static AirportService instance;
    private final AirportDAO airportDAO;

    private AirportService() {
        this.airportDAO = AirportDAO.getInstance();
    }

    public static AirportService getInstance() {
        if (instance == null) {
            synchronized (AirportService.class) {
                if (instance == null) {
                    instance = new AirportService();
                }
            }
        }
        return instance;
    }

    public void addAirport(Airport airport) {
        Set<Airport> existingAirports = getAllAirports();
        if (existingAirports.contains(airport)) {
            System.out.println("Airport already exists: " + airport.getName());
        } else {
            airportDAO.insertAirport(airport);
            System.out.println("Airport added to database: " + airport.getName());
        }
        AuditService.getInstance().logAction("Added airport: " + airport.getName());
    }

    public void updateAirport(int id, String name) {
        if (airportDAO.updateAirport(id, name) == 0) {
            System.out.println("Airport not found with ID: " + id);
        } else {
            System.out.println("Airport updated in database: " + name);
        }
        AuditService.getInstance().logAction("Updated airport with ID: " + id + " to name: " + name);
    }

    public void removeAirport(Airport airport) {
        if (airportDAO.deleteAirport(airport.getId()) == 0) {
            System.out.println("Airport not found with ID: " + airport.getId());
        } else {
            System.out.println("Airport deleted from database: " + airport.getName());
        }
        AuditService.getInstance().logAction("Deleted airport with ID: " + airport.getId());
    }

    public Set<Airport> getAllAirports() {
        List<Airport> list = airportDAO.getAllAirports();
        return new HashSet<>(list);
    }

    public Airport getAirportById(int id) {
        for (Airport airport : getAllAirports()) {
            if (airport.getId() == id) {
                return airport;
            }
        }
        return null;
    }

    public Airport getAirportByName(String name) {
        for (Airport airport : getAllAirports()) {
            if (airport.getName().equalsIgnoreCase(name)) {
                return airport;
            }
        }
        return null;
    }
}