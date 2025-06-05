import Domain.*;
import Services.PassengerAircraftService;
import Services.AirportService;
import Services.FlightService;
import Utils.AircraftType;

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

        PassengerAircraftService passengerAircraftService = PassengerAircraftService.getInstance();
        AirportService airportService = AirportService.getInstance();
        FlightService flightService = FlightService.getInstance();

        System.out.println("Airports loaded");
        System.out.println("Aircraft loaded");
        System.out.println("Flights loaded");
        System.out.println("Reservations loaded");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Book a seat on a flight");
            System.out.println("2. View all destinations");
            System.out.println("3. View all flights");
            System.out.println("4. View flights from a specific location");
            System.out.println("5. View passengers for a flight");
            System.out.println("6. View all flights for a passenger");
            System.out.println("7. View all passenger aircraft");
            System.out.println("8. View all cargo aircraft");
            System.out.println("9. Add a new flight");
            System.out.println("10. Add a new airport");
            System.out.println("11. Add a new passenger aircraft");
            System.out.println("12. Add a new cargo aircraft");
            System.out.println("13. Update a flight's details");
            System.out.println("14. Update an airport's name");
            System.out.println("15. Update a passenger aircraft's capacity");
            System.out.println("16. Update a cargo aircraft's capacity");
            System.out.println("17. Remove a passenger aircraft");
            System.out.println("18. Remove a cargo aircraft");
            System.out.println("19. Remove an airport");
            System.out.println("20. Remove a passenger from a flight");
            System.out.println("21. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    try {
                        // 1. Afișează aeroporturile din care există zboruri
                        List<Flight> allFlights = flightService.getAllFlights();
                        java.util.Set<Integer> departureAirportIds = new java.util.HashSet<>();
                        for (Flight f : allFlights) {
                            departureAirportIds.add(f.getDepartureAirportId());
                        }
                        java.util.Map<Integer, String> airportIdToName20 = new java.util.HashMap<>();
                        for (Airport airport : airportService.getAllAirports()) {
                            airportIdToName20.put(airport.getId(), airport.getName());
                        }
                        System.out.println("Available departure airports:");
                        List<Airport> departureAirports = new java.util.ArrayList<>();
                        for (Integer id : departureAirportIds) {
                            Airport airport = airportService.getAirportById(id);
                            departureAirports.add(airport);
                        }
                        for (Airport airport : departureAirports) {
                            System.out.println("- " + airport.getName() + " (" + airport.getCity() + ")");
                        }
                        System.out.println("Enter the name of the departure airport (e.g. OTP, CDG) or type 'x' to go back:");
                        String depAirportNameInput = getInputOrBack(scanner).trim();
                        Airport selectedDepartureAirport = null;
                        for (Airport airport : departureAirports) {
                            if (airport.getName().equalsIgnoreCase(depAirportNameInput)) {
                                selectedDepartureAirport = airport;
                                break;
                            }
                        }
                        if (selectedDepartureAirport == null) {
                            System.out.println("Invalid airport code.");
                            break;
                        }
                        int depId = selectedDepartureAirport.getId();

                        // 2. Afișează aeroporturile destinație pentru zborurile cu plecare din aeroportul selectat
                        java.util.Set<Integer> arrivalAirportIds = new java.util.HashSet<>();
                        for (Flight f : allFlights) {
                            if (f.getDepartureAirportId() == depId) {
                                arrivalAirportIds.add(f.getArrivalAirportId());
                            }
                        }
                        if (arrivalAirportIds.isEmpty()) {
                            System.out.println("No destinations found for this airport.");
                            break;
                        }
                        System.out.println("Available destination airports:");
                        List<Airport> arrivalAirports = new java.util.ArrayList<>();
                        for (Integer id : arrivalAirportIds) {
                            Airport airport = airportService.getAirportById(id);
                            arrivalAirports.add(airport);
                        }
                        for (Airport airport : arrivalAirports) {
                            System.out.println("- " + airport.getName() + " (" + airport.getCity() + ")");
                        }
                        System.out.println("Enter the name of the destination airport (e.g. OTP, CDG) or type 'x' to go back:");
                        String arrAirportNameInput = getInputOrBack(scanner).trim();
                        Airport selectedArrivalAirport = null;
                        for (Airport airport : arrivalAirports) {
                            if (airport.getName().equalsIgnoreCase(arrAirportNameInput)) {
                                selectedArrivalAirport = airport;
                                break;
                            }
                        }
                        if (selectedArrivalAirport == null) {
                            System.out.println("Invalid airport code.");
                            break;
                        }
                        int arrId = selectedArrivalAirport.getId();

                        // 3. Afișează zborurile disponibile pe ruta selectată
                        List<Flight> routeFlights = new java.util.ArrayList<>();
                        for (Flight f : allFlights) {
                            if (f.getDepartureAirportId() == depId && f.getArrivalAirportId() == arrId) {
                                routeFlights.add(f);
                            }
                        }
                        if (routeFlights.isEmpty()) {
                            System.out.println("No flights found for this route.");
                            break;
                        }
                        System.out.println("Available flights:");
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        for (Flight f : routeFlights) {
                            String depTime = f.getDepartureDateTime().format(formatter);
                            String arrTime = f.getArrivalDateTime().format(formatter);
                            System.out.println("Flight number: " + f.getFlightNumber() + ", Departure: " + depTime + ", Arrival: " + arrTime);
                        }
                        System.out.println("Enter flight number to add a passenger (or type 'x' to go back):");
                        String flightNumberInput = getInputOrBack(scanner);
                        int flightNumber = Integer.parseInt(flightNumberInput);
                        Flight selectedFlight = null;
                        for (Flight f : routeFlights) {
                            if (f.getFlightNumber() == flightNumber) {
                                selectedFlight = f;
                                break;
                            }
                        }
                        if (selectedFlight == null) {
                            System.out.println("Error: Flight not found on this route.");
                            break;
                        }
                        // 4. Adaugă pasager (ca la case 3)
                        while (true) {
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
                            System.out.println("Do you want to add another passenger to this flight? (y/n)");
                            String again = scanner.nextLine().trim();
                            if (!again.equalsIgnoreCase("y")) {
                                break;
                            }
                        }
                    } catch (RuntimeException e) {
                        if (!e.getMessage().equals("back")) throw e;
                    }
                    break;

                case 2:
                    System.out.println("Available cities:");
                    Set<Airport> allAirports = airportService.getAllAirports();
                    java.util.Set<String> cities = new java.util.TreeSet<>();
                    for (Airport airport : allAirports) {
                        cities.add(airport.getCity());
                    }
                    for (String cityName : cities) {
                        System.out.println(cityName);
                    }
                    break;

                case 3:
                    List<Flight> flights = flightService.getAllFlights();
                    if (flights.isEmpty()) {
                        System.out.println("No flights available.");
                        break;
                    }
                    java.util.Map<Integer, String> airportIdToName = new java.util.HashMap<>();
                    for (Airport airport : airportService.getAllAirports()) {
                        airportIdToName.put(airport.getId(), airport.getName());
                    }
                    for (Flight flight : flights) {
                        String departureName = airportIdToName.getOrDefault(flight.getDepartureAirportId(), "Unknown");
                        String arrivalName = airportIdToName.getOrDefault(flight.getArrivalAirportId(), "Unknown");
                        System.out.println("Flight " + flight.getFlightNumber() + " from " +
                                departureName + " to " + arrivalName +
                                " using aircraft ID " + flight.getAircraftId());
                    }
                    break;

                case 4:
                    try {
                        System.out.println("Enter departure city (or type 'x' to go back):");
                        String city = getInputOrBack(scanner);
                        List<Flight> cityFlights = flightService.getFlightsByDepartureCity(city);
                        java.util.Map<Integer, String> airportIdToName2 = new java.util.HashMap<>();
                        for (Airport airport : airportService.getAllAirports()) {
                            airportIdToName2.put(airport.getId(), airport.getName());
                        }
                        if (cityFlights.isEmpty()) {
                            System.out.println("No flights found for the specified city.");
                        } else {
                            for (Flight flight : cityFlights) {
                                String departureName = airportIdToName2.getOrDefault(flight.getDepartureAirportId(), "Unknown");
                                String arrivalName = airportIdToName2.getOrDefault(flight.getArrivalAirportId(), "Unknown");
                                System.out.println("Flight " + flight.getFlightNumber() + " from " +
                                        departureName + " to " + arrivalName +
                                        " using aircraft ID " + flight.getAircraftId());
                            }
                        }
                    } catch (RuntimeException e) {
                        if (!e.getMessage().equals("back")) throw e;
                    }
                    break;

                case 5:
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

                case 6:
                    try {
                        System.out.println("Enter passenger name and surname (or type 'x' to go back):");
                        String passengerName = getInputOrBack(scanner);
                        List<String> details = flightService.getFlightDetailsByPassengerName(passengerName);
                        for (String detail : details) {
                            System.out.println(detail);
                        }
                    } catch (RuntimeException e) {
                        if (!e.getMessage().equals("back")) throw e;
                    }
                    break;

                case 7:
                    System.out.println("Available passenger aircraft:");
                    List<PassengerAircraft> passengerAircraftList = Services.PassengerAircraftService.getInstance().getAllPassengerAircraft();
                    for (PassengerAircraft aircraft : passengerAircraftList) {
                        System.out.println("Passenger Aircraft ID: " + aircraft.getId() + ", Type: " +
                                aircraft.getAircraftType() + ", Total Seats: " + aircraft.getTotalSeats());
                    }
                    break;

                case 8:
                    System.out.println("Available cargo aircraft:");
                    List<Domain.CargoAircraft> cargoAircraftList = Services.CargoAircraftService.getInstance().getAllCargoAircraft();
                    for (Domain.CargoAircraft aircraft : cargoAircraftList) {
                        System.out.println("Cargo Aircraft ID: " + aircraft.getId() + ", Type: " +
                                aircraft.getAircraftType() + ", Cargo Capacity: " + aircraft.getCargoCapacity() + " tone");
                    }
                    break;

                case 9:
                    try {
                        System.out.println("Enter the name of the departure airport (e.g. OTP, CDG) (or type 'x' to go back):");
                        String departureIata = getInputOrBack(scanner);
                        Airport departureAirport = airportService.getAirportByName(departureIata);
                        if (departureAirport == null) {
                            System.out.println("Invalid departure airport code.");
                            break;
                        }
                        int departureAirportId = departureAirport.getId();

                        System.out.println("Enter the name of the destination airport (e.g. OTP, CDG) (or type 'x' to go back):");
                        String arrivalIata = getInputOrBack(scanner);
                        Airport arrivalAirport = airportService.getAirportByName(arrivalIata);
                        if (arrivalAirport == null) {
                            System.out.println("Invalid arrival airport code.");
                            break;
                        }
                        int arrivalAirportId = arrivalAirport.getId();

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

                case 10:
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

                case 11:
                    try {
                        System.out.println("Enter aircraft type (Boeing/Airbus) (or type 'x' to go back):");
                        String aircraftTypeInput = getInputOrBack(scanner);
                        AircraftType aircraftType = AircraftType.valueOf(aircraftTypeInput);

                        System.out.println("Enter total seats (or type 'x' to go back):");
                        String totalSeatsInput = getInputOrBack(scanner);
                        int totalSeats = Integer.parseInt(totalSeatsInput);

                        PassengerAircraft newAircraft = new PassengerAircraft(0, aircraftType, totalSeats);
                        passengerAircraftService.addPassengerAircraft(newAircraft);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: Invalid aircraft type.");
                    } catch (RuntimeException e) {
                        if (!e.getMessage().equals("back")) throw e;
                    }
                    break;
                case 12:
                    try {
                        System.out.println("Enter aircraft type (Boeing/Airbus) (or type 'x' to go back):");
                        String aircraftTypeInput = getInputOrBack(scanner);
                        AircraftType aircraftType = AircraftType.valueOf(aircraftTypeInput);

                        System.out.println("Enter cargo capacity (tone) (or type 'x' to go back):");
                        String cargoCapacityInput = getInputOrBack(scanner);
                        double cargoCapacity = Double.parseDouble(cargoCapacityInput);

                        Domain.CargoAircraft newCargoAircraft = new Domain.CargoAircraft(0, aircraftType, (int) cargoCapacity);
                        Services.CargoAircraftService.getInstance().addCargoAircraft(newCargoAircraft);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: Invalid aircraft type.");
                    } catch (RuntimeException e) {
                        if (!e.getMessage().equals("back")) throw e;
                    }
                    break;

                case 13:
                    try {
                        System.out.println("Enter flight number to update (or type 'x' to go back):");
                        String flightNumberInput = getInputOrBack(scanner);
                        int flightNumber = Integer.parseInt(flightNumberInput);
                        Flight flight = flightService.findFlightByNumber(flightNumber);
                        if (flight == null) {
                            System.out.println("Error: Flight not found.");
                            break;
                        }
                        System.out.println("Enter new departure Airport ID (or type 'x' to go back):");
                        int newDepartureAirportId = Integer.parseInt(getInputOrBack(scanner));
                        System.out.println("Enter new arrival Airport ID (or type 'x' to go back):");
                        int newArrivalAirportId = Integer.parseInt(getInputOrBack(scanner));
                        System.out.println("Enter new departure date (yyyy-mm-dd) (or type 'x' to go back):");
                        String newDepartureDateStr = getInputOrBack(scanner);
                        System.out.println("Enter new departure time (hh:mm) (or type 'x' to go back):");
                        String newDepartureTimeStr = getInputOrBack(scanner);
                        LocalDateTime newDepartureDateTime = LocalDateTime.parse(
                                newDepartureDateStr + newDepartureTimeStr,
                                DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm")
                        );
                        System.out.println("Enter new arrival date (yyyy-mm-dd) (or type 'x' to go back):");
                        String newArrivalDateStr = getInputOrBack(scanner);
                        System.out.println("Enter new arrival time (hh:mm) (or type 'x' to go back):");
                        String newArrivalTimeStr = getInputOrBack(scanner);
                        LocalDateTime newArrivalDateTime = LocalDateTime.parse(
                                newArrivalDateStr + newArrivalTimeStr,
                                DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm")
                        );
                        System.out.println("Enter new aircraft ID (or type 'x' to go back):");
                        int newAircraftId = Integer.parseInt(getInputOrBack(scanner));
                        Flight updatedFlight = new Flight(
                                newDepartureAirportId,
                                newArrivalAirportId,
                                newDepartureDateTime,
                                newArrivalDateTime,
                                newAircraftId
                        );
                        java.lang.reflect.Field field = Flight.class.getDeclaredField("flightNumber");
                        field.setAccessible(true);
                        field.set(updatedFlight, flightNumber);
                        flightService.updateFlight(updatedFlight);
                        System.out.println("Flight updated successfully.");
                    } catch (RuntimeException | NoSuchFieldException | IllegalAccessException e) {
                        if (e instanceof RuntimeException && !e.getMessage().equals("back")) throw (RuntimeException)e;
                        if (!(e instanceof RuntimeException)) {
                            System.out.println("Error: Could not update flight.");
                        }
                    }
                    break;

                case 14:
                    try {
                        System.out.println("Enter airport ID to update (or type 'x' to go back):");
                        String idInput = getInputOrBack(scanner);
                        int airportId = Integer.parseInt(idInput);

                        System.out.println("Enter new airport name (or type 'x' to go back):");
                        String newName = getInputOrBack(scanner);

                        airportService.updateAirport(airportId, newName);
                    } catch (RuntimeException e) {
                        if (!e.getMessage().equals("back")) throw e;
                    }
                    break;


                case 15:
                    try {
                        System.out.println("Enter aircraft ID to update (or type 'x' to go back):");
                        String idInput = getInputOrBack(scanner);
                        int aircraftId = Integer.parseInt(idInput);

                        PassengerAircraft aircraft = null;
                        for (PassengerAircraft a : passengerAircraftService.getAllPassengerAircraft()) {
                            if (a.getId() == aircraftId) {
                                aircraft = a;
                                break;
                            }
                        }
                        if (aircraft == null) {
                            System.out.println("Error: Passenger aircraft not found.");
                            break;
                        }

                        System.out.println("Enter new total seats (or type 'x' to go back):");
                        String seatsInput = getInputOrBack(scanner);
                        int newSeats = Integer.parseInt(seatsInput);

                        PassengerAircraft updatedAircraft = new PassengerAircraft(aircraftId, aircraft.getAircraftType(), newSeats);
                        passengerAircraftService.updatePassengerAircraft(updatedAircraft);
                        System.out.println("Passenger aircraft updated successfully.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: Invalid input.");
                    } catch (RuntimeException e) {
                        if (!e.getMessage().equals("back")) throw e;
                    }
                    break;

                case 16:
                    try {
                        System.out.println("Enter cargo aircraft ID to update (or type 'x' to go back):");
                        String idInput = getInputOrBack(scanner);
                        int aircraftId = Integer.parseInt(idInput);

                        Domain.CargoAircraft aircraft = null;
                        for (Domain.CargoAircraft a : Services.CargoAircraftService.getInstance().getAllCargoAircraft()) {
                            if (a.getId() == aircraftId) {
                                aircraft = a;
                                break;
                            }
                        }
                        if (aircraft == null) {
                            System.out.println("Error: Cargo aircraft not found.");
                            break;
                        }

                        System.out.println("Enter new cargo capacity (tone) (or type 'x' to go back):");
                        String capacityInput = getInputOrBack(scanner);
                        int newCapacity = Integer.parseInt(capacityInput);

                        Domain.CargoAircraft updatedAircraft = new Domain.CargoAircraft(aircraftId, aircraft.getAircraftType(), newCapacity);
                        Services.CargoAircraftService.getInstance().updateCargoAircraft(updatedAircraft);
                        System.out.println("Cargo aircraft updated successfully.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: Invalid input.");
                    } catch (RuntimeException e) {
                        if (!e.getMessage().equals("back")) throw e;
                    }
                    break;

                case 17:
                    try {
                        System.out.println("Enter aircraft ID to delete (or type 'x' to go back):");
                        String idInput = getInputOrBack(scanner);
                        int aircraftId = Integer.parseInt(idInput);
                        boolean hasFlights = false;
                        for (Flight f : flightService.getAllFlights()) {
                            if (f.getAircraftId() == aircraftId) {
                                hasFlights = true;
                                break;
                            }
                        }
                        if (hasFlights) {
                            System.out.println("Error: Aircraft cannot be deleted because it is assigned to one or more flights.");
                        } else {
                            Services.PassengerAircraftService.getInstance().deletePassengerAircraft(aircraftId);
                            System.out.println("Passenger aircraft deleted successfully.");
                        }
                    } catch (RuntimeException e) {
                        if (!e.getMessage().equals("back")) throw e;
                    }
                    break;

                case 18:
                    try {
                        System.out.println("Enter cargo aircraft ID to delete (or type 'x' to go back):");
                        String idInput = getInputOrBack(scanner);
                        int aircraftId = Integer.parseInt(idInput);
                        boolean hasFlights = false;
                        for (Flight f : flightService.getAllFlights()) {
                            if (f.getAircraftId() == aircraftId) {
                                hasFlights = true;
                                break;
                            }
                        }
                        if (hasFlights) {
                            System.out.println("Error: Cargo aircraft cannot be deleted because it is assigned to one or more flights.");
                        } else {
                            Services.CargoAircraftService.getInstance().deleteCargoAircraft(aircraftId);
                            System.out.println("Cargo aircraft deleted successfully.");
                        }
                    } catch (RuntimeException e) {
                        if (!e.getMessage().equals("back")) throw e;
                    }
                    break;

                case 19:
                    try {
                        System.out.println("Enter airport ID to delete (or type 'x' to go back):");
                        String idInput = getInputOrBack(scanner);
                        int airportId = Integer.parseInt(idInput);
                        Airport airportToRemove = null;
                        for (Airport airport : airportService.getAllAirports()) {
                            if (airport.getId() == airportId) {
                                airportToRemove = airport;
                                break;
                            }
                        }
                        if (airportToRemove == null) {
                            System.out.println("Error: Airport not found.");
                        } else {
                            boolean hasFlights = false;
                            for (Flight f : flightService.getAllFlights()) {
                                if (f.getDepartureAirportId() == airportId || f.getArrivalAirportId() == airportId) {
                                    hasFlights = true;
                                    break;
                                }
                            }
                            if (hasFlights) {
                                System.out.println("Error: Airport cannot be deleted because it is used in one or more flights.");
                            } else {
                                airportService.removeAirport(airportToRemove);
                            }
                        }
                    } catch (RuntimeException e) {
                        if (!e.getMessage().equals("back")) throw e;
                    }
                    break;

                case 20:
                    try {
                        System.out.println("Enter passenger name and surname (or type 'x' to go back):");
                        String passengerName = getInputOrBack(scanner);
                        List<Integer> flightsWithPassenger = flightService.getFlightNumbersByPassengerName(passengerName);
                        if (flightsWithPassenger.isEmpty()) {
                            System.out.println("Passenger not found on any flight.");
                            break;
                        }
                        System.out.println("Passenger '" + passengerName + "' is on the following flights:");
                        for (Integer fn : flightsWithPassenger) {
                            System.out.println("Flight number: " + fn);
                        }
                        System.out.println("Enter flight number to remove passenger from (or type 'x' to go back):");
                        String flightNumberInput = getInputOrBack(scanner);
                        int flightNumber = Integer.parseInt(flightNumberInput);
                        if (!flightsWithPassenger.contains(flightNumber)) {
                            System.out.println("Error: Passenger is not on this flight.");
                            break;
                        }
                        flightService.cancelSeat(flightNumber, passengerName);
                    } catch (RuntimeException e) {
                        if (!e.getMessage().equals("back")) throw e;
                    }
                    break;

                case 21:
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
