import Domain.*;
import Services.AircraftService;
import Services.AirportService;
import Services.FlightService;
import Utils.AircraftType;
import Utils.SeatPosition;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static String getInputOrBack(Scanner scanner) {
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("x")) {
            throw new RuntimeException("back");
        }
        return input;
    }

    public static void main(String[] args) {

        AircraftService aircraftService = AircraftService.getInstance();
        AirportService airportService = AirportService.getInstance();
        FlightService flightService = FlightService.getInstance();

        System.out.println("Airports loaded");
        System.out.println("Aircraft loaded");
        System.out.println("Flights loaded");
        System.out.println("Reservations loaded");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("2. View all flights");
            System.out.println("3. View flights from a specific location");
            System.out.println("4. Add a passenger to a flight");
            System.out.println("5. Add a new flight");
            System.out.println("6. View passengers for a flight");
            System.out.println("7. View all aircraft");
            System.out.println("8. View all airports");
            System.out.println("9. Add a new airport");
            System.out.println("10. Add a new aircraft");
            System.out.println("11. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {


                case 2:
                    List<Flight> flights = flightService.getAllFlights();
                    for (Flight flight : flights) {
                        System.out.println("Flight " + flight.getFlightNumber() + " from " +
                                flight.getDepartureAirportId() + " to " + flight.getArrivalAirportId() +
                                " using aircraft ID " + flight.getAircraftId());
                    }
                    break;

                case 3:
                    try {
                        System.out.println("Enter departure city (or type 'x' to go back):");
                        String city = getInputOrBack(scanner);
                        List<Flight> cityFlights = flightService.getFlightsByDepartureCity(city);
                        for (Flight flight : cityFlights) {
                            System.out.println("Flight " + flight.getFlightNumber() + " from " +
                                    flight.getDepartureAirportId() + " to " + flight.getArrivalAirportId() +
                                    " using aircraft ID " + flight.getAircraftId());
                        }
                    } catch (RuntimeException e) {
                        if (!e.getMessage().equals("back")) throw e;
                    }
                    break;

                case 4:
                    try {
                        System.out.println("Enter flight number (or type 'x' to go back):");
                        String flightNumberInput = getInputOrBack(scanner);
                        int flightNumber = Integer.parseInt(flightNumberInput);

                        Flight flight = flightService.findFlightByNumber(flightNumber);
                        if (flight == null) {
                            System.out.println("Error: Flight not found.");
                            break;
                        }

                        System.out.println("Enter passenger first name (or type 'x' to go back):");
                        String passengerFirstName = getInputOrBack(scanner);
                        System.out.println("Enter passenger last name (or type 'x' to go back):");
                        String passengerLastName = getInputOrBack(scanner);

                        if (passengerFirstName.isEmpty() || passengerLastName.isEmpty()) {
                            System.out.println("Error: Invalid passenger details.");
                            break;
                        }

                        String passengerName = passengerFirstName + " " + passengerLastName;

                        System.out.println("Enter seat row (or type 'x' to go back):");
                        String rowInput = getInputOrBack(scanner);
                        int row = Integer.parseInt(rowInput);

                        System.out.println("Enter seat position (A/B/C/D/E/F) (or type 'x' to go back):");
                        String positionInput = getInputOrBack(scanner);
                        char position = positionInput.charAt(0);

                        Seat seat = new Seat(row, position, false, passengerName, flightNumber);
                        flightService.reserveSeat(seat);
                        System.out.println("Passenger added to flight: " + flightNumber);
                    } catch (RuntimeException e) {
                        if (!e.getMessage().equals("back")) throw e;
                    }
                    break;

                case 5:
                    try {
                        System.out.println("Enter departure Airport ID (or type 'x' to go back):");
                        int departureAirportId = Integer.parseInt(getInputOrBack(scanner));

                        System.out.println("Enter arrival AirportId (or type 'x' to go back):");
                        int arrivalAirportId = Integer.parseInt(getInputOrBack(scanner));

                        System.out.println("Enter departure date (yyyy-mm-dd) (or type 'x' to go back):");
                        String departureDateStr = getInputOrBack(scanner);

                        System.out.println("Enter departure time (hh:mm) (or type 'x' to go back):");
                        String departureTimeStr = getInputOrBack(scanner);
                        LocalDateTime departureDateTime = LocalDateTime.parse(
                                departureDateStr + departureTimeStr,
                                DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm")
                        );

                        System.out.println("Enter arrival date (yyyy-mm-dd) (or type 'x' to go back):");
                        String arrivalDateStr = getInputOrBack(scanner);

                        System.out.println("Enter arrival time (hh:mm) (or type 'x' to go back):");
                        String arrivalTimeStr = getInputOrBack(scanner);
                        LocalDateTime arrivalDateTime = LocalDateTime.parse(
                                arrivalDateStr + arrivalTimeStr,
                                DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm")
                        );

                        System.out.println("Enter aircraft ID (or type 'x' to go back):");
                        String aircraftIdInput = getInputOrBack(scanner);
                        int aircraftId = Integer.parseInt(aircraftIdInput);

                        Flight newFlight = new Flight(departureAirportId, arrivalAirportId,
                                departureDateTime, arrivalDateTime, aircraftId);

                        flightService.addFlight(newFlight);
                        System.out.println("Flight added: " + newFlight);
                    } catch (RuntimeException e) {
                        if (!e.getMessage().equals("back")) throw e;
                    }
                    break;

                case 6:
                    try {
                        System.out.println("Enter flight number (or type 'x' to go back):");
                        String flightNumberInput = getInputOrBack(scanner);
                        int flightNumber = Integer.parseInt(flightNumberInput);

                        List<String> passengers = flightService.getPassengersForFlight(flightNumber);
                        if (passengers.isEmpty()) {
                            System.out.println("No passengers found for this flight.");
                        } else {
                            System.out.println("Passengers for flight " + flightNumber + ":");
                            for (String passenger : passengers) {
                                System.out.println("Passenger: " + passenger);
                            }
                        }
                    } catch (RuntimeException e) {
                        if (!e.getMessage().equals("back")) throw e;
                    }
                    break;

                case 7:
                    System.out.println("Available aircraft:");
                    List<Aircraft> aircraftList = aircraftService.getAllAircraft();
                    for (Aircraft aircraft : aircraftList) {
                        System.out.println("Aircraft ID: " + aircraft.getId() + ", Type: " +
                                aircraft.getAircraftType() + ", Total Seats: " + aircraft.getTotalSeats());
                    }
                    break;

                case 8:
                    System.out.println("Available airports:");
                    Set<Airport> airports = airportService.getAllAirports();
                    for (Airport airport : airports) {
                        System.out.println(airport.getName());
                    }
                    break;

                case 9:
                    try {
                        System.out.println("Enter the name of the airport (or type 'x' to go back):");
                        String airportName = getInputOrBack(scanner);

                        System.out.println("Enter the city (or type 'x' to go back):");
                        String city = getInputOrBack(scanner);

                        System.out.println("Enter the country (or type 'x' to go back):");
                        String country = getInputOrBack(scanner);

                        Airport newAirport = new Airport(airportName, city, country); // ID will be set by database
                        airportService.addAirport(newAirport);
                    } catch (RuntimeException e) {
                        if (!e.getMessage().equals("back")) throw e;
                    }
                    break;

                case 10:
                    try {
                        System.out.println("Enter aircraft type (Boeing/Airbus) (or type 'x' to go back):");
                        String aircraftTypeInput = getInputOrBack(scanner);
                        AircraftType aircraftType = AircraftType.valueOf(aircraftTypeInput);

                        System.out.println("Enter total seats (or type 'x' to go back):");
                        String totalSeatsInput = getInputOrBack(scanner);
                        int totalSeats = Integer.parseInt(totalSeatsInput);

                        Aircraft newAircraft = new Aircraft(0, aircraftType, totalSeats); // ID will be set by database
                        aircraftService.addAircraft(newAircraft);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: Invalid aircraft type.");
                    } catch (RuntimeException e) {
                        if (!e.getMessage().equals("back")) throw e;
                    }
                    break;

                case 11:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}