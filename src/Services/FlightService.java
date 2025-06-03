package Services;

import DAO.AircraftDAO;
import DAO.AirportDAO;
import DAO.FlightDAO;
import DAO.SeatDAO;
import Domain.*;
import Utils.AircraftType;
import Utils.SeatPosition;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FlightService {
    private static FlightService instance;
    private final FlightDAO flightDAO;
    private final AircraftDAO aircraftDAO;
    private final SeatDAO seatDAO;
    private final AirportDAO airportDAO;

    private FlightService() {
        this.flightDAO = FlightDAO.getInstance();
        this.aircraftDAO = AircraftDAO.getInstance();
        this.seatDAO = SeatDAO.getInstance();
        this.airportDAO = AirportDAO.getInstance();
    }

    public static FlightService getInstance() {
        if (instance == null) {
            synchronized (FlightService.class) {
                if (instance == null) {
                    instance = new FlightService();
                }
            }
        }
        return instance;
    }

    public void addFlight(Flight flight) {
        if (flight.getDepartureDateTime().isAfter(flight.getArrivalDateTime())) {
            System.out.println("Error: Departure time must be before arrival time.");
            return;
        }

        if (flight.getDepartureAirportId() == flight.getArrivalAirportId()) {
            System.out.println("Error: Departure and arrival airports must be different.");
            return;
        }

        if (flight.getAircraftId() <= 0) {
            System.out.println("Error: Invalid aircraft ID.");
            return;
        }

        for (Flight aircraftFlight : flightDAO.getFlightsByAircraftId(flight.getAircraftId())) {
            if (aircraftFlight.getDepartureDateTime().isBefore(flight.getArrivalDateTime()) &&
                    aircraftFlight.getArrivalDateTime().isAfter(flight.getDepartureDateTime())) {
                System.out.println("Error: Aircraft is already scheduled for this time.");
                return;
            }
        }

        int generatedID = flightDAO.insertFlight(flight);
        System.out.println("Flight added to database: " + flight);

        Aircraft aircraft = aircraftDAO.getAllAircraft().stream()
                .filter(a -> a.getId() == flight.getAircraftId())
                .findFirst()
                .orElse(null);

        if (aircraft == null) {
            System.out.println("Aircraft not found for ID: " + flight.getAircraftId());
            return;
        }

        SeatPosition[] positions = SeatPosition.values();
        int totalSeats = aircraft.getTotalSeats();
        int totalRows = totalSeats / positions.length;

        for (int row = 1; row <= totalRows; row++) {
            for (int i = 0; i < positions.length; i++) {
                boolean extraLegRoom =
                        ((aircraft.getAircraftType() == AircraftType.Airbus) && row <= totalRows / 5) ||
                                ((aircraft.getAircraftType() == AircraftType.Boeing) && row <= totalRows / 10);

                Seat seat = new Seat(row, positions[i].name().charAt(0), extraLegRoom, null, generatedID);
                seatDAO.insertSeat(seat);
            }
        }

        System.out.println("Seats generated for flight: " + totalSeats);
        AuditService.getInstance().logAction("Flight added to database: " + flight);
    }

    public void removeFlight(int flightNumber) {
        if(flightDAO.deleteFlight(flightNumber) == 0) {;
            System.out.println("Error: Flight not found with number " + flightNumber);
            return;
        }
        System.out.println("Flight with number " + flightNumber + " was removed.");
        seatDAO.deleteSeatsByFlightId(flightNumber);
        System.out.println("Seats for flight " + flightNumber + " were removed.");
        AuditService.getInstance().logAction("Flight removed from database: " + flightNumber);
    }

    public Flight findFlightByNumber(int flightNumber) {
        return flightDAO.getAllFlights().stream()
                .filter(f -> f.getFlightNumber() == flightNumber)
                .findFirst()
                .orElse(null);
    }

    public List<Flight> getAllFlights() {
        return flightDAO.getAllFlights();
    }

    public void updateFlight(Flight flight) {
        if (flight.getDepartureDateTime().isAfter(flight.getArrivalDateTime())) {
            System.out.println("Error: Departure time must be before arrival time.");
            return;
        }

        if (flight.getDepartureAirportId() == flight.getArrivalAirportId()) {
            System.out.println("Error: Departure and arrival airports must be different.");
            return;
        }

        if (flight.getAircraftId() <= 0) {
            System.out.println("Error: Invalid aircraft ID.");
            return;
        }

        for (Flight aircraftFlight : flightDAO.getFlightsByAircraftId(flight.getAircraftId())) {
            if (aircraftFlight.getDepartureDateTime().isBefore(flight.getArrivalDateTime()) &&
                    aircraftFlight.getArrivalDateTime().isAfter(flight.getDepartureDateTime())) {
                System.out.println("Error: Aircraft is already scheduled for this time.");
                return;
            }
        }

        if(flightDAO.updateFlight(flight) == 0) {;
            System.out.println("Error: Flight not found with number " + flight.getFlightNumber());
            return;
        }

        System.out.println("Flight updated: " + flight);
        AuditService.getInstance().logAction("Flight updated in database: " + flight);
    }

    public void reserveSeat(Seat seat) {

        if (seatDAO.updateSeat(seat) == 0) {;
            System.out.println("Error: Seat already reserved.");
            return;
        }

        seatDAO.updateSeat(seat);
        System.out.println("Seat reserved: " + seat);
        AuditService.getInstance().logAction("Seat reserved: " + seat);
    }

    public void cancelSeat(int FlightNumber, String passengerName) {
        if (seatDAO.deleteSeat(FlightNumber, passengerName) == 0) {
            System.out.println("Error: Passenger not in this flight " + passengerName);
            return;
        }

        System.out.println("Seat cancelled for passenger: " + passengerName);
        AuditService.getInstance().logAction("Seat cancelled for passenger: " + passengerName);
    }

    public List<String> getPassengersForFlight(int flightNumber) {
        List<String> passengerNames = new ArrayList<>();
        List<Seat> seats = seatDAO.getAllSeats();
        for (Seat seat : seats) {
            if (seat.getFlightId() == flightNumber && seat.getPassengerName() != null) {
                passengerNames.add(seat.getPassengerName());
            }
        }
        return passengerNames;
    }

    public List<Flight> getFlightsByDepartureTime() {
        List<Flight> flights = getAllFlights();
        flights.sort(Comparator.comparing(Flight::getDepartureDateTime));
        return flights;
    }

    public List<Flight> getFlightsByDepartureCity(String city) {
        List<Flight> flights = getAllFlights();
        List<Flight> filteredFlights = new ArrayList<>();
        for (Airport airport : airportDAO.getAirportsByCity(city)) {
            for (Flight flight : flights) {
                if (flight.getDepartureAirportId() == airport.getId()) {
                    filteredFlights.add(flight);
                }
            }
        }
        return filteredFlights;
    }

    public List<Flight> getFlightsByPassengerName(String passengerName) {
        return flightDAO.getFlightsByPassengerName(passengerName);
    }

    public List<String> getFlightDetailsByPassengerName(String passengerName) {
        List<String> details = new ArrayList<>();
        List<Flight> flights = getFlightsByPassengerName(passengerName);
        if (flights.isEmpty()) {
            details.add("Passenger not found on any flight.");
            return details;
        }
        List<Airport> airports = airportDAO.getAllAirports();
        for (Flight flight : flights) {
            Airport dep = airports.stream()
                .filter(a -> a.getId() == flight.getDepartureAirportId())
                .findFirst().orElse(null);
            Airport arr = airports.stream()
                .filter(a -> a.getId() == flight.getArrivalAirportId())
                .findFirst().orElse(null);
            String depName = dep != null ? dep.getName() + ", " + dep.getCity() : "Unknown";
            String arrName = arr != null ? arr.getName() + ", " + arr.getCity() : "Unknown";
            details.add("Flight number: " + flight.getFlightNumber() +
                ", From: " + depName +
                ", To: " + arrName +
                ", Departure: " + flight.getDepartureDateTime() +
                ", Arrival: " + flight.getArrivalDateTime());
        }
        return details;
    }

    public List<Integer> getFlightNumbersByPassengerName(String passengerName) {
        List<Integer> flightNumbers = new ArrayList<>();
        List<Flight> flights = getFlightsByPassengerName(passengerName);
        for (Flight flight : flights) {
            flightNumbers.add(flight.getFlightNumber());
        }
        return flightNumbers;
    }
}
