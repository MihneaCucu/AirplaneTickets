package Domain;
import java.time.LocalDateTime;

public class Flight {
    private int flightNumber;
    private int departureAirportId;
    private int arrivalAirportId;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
    private int aircraftId;



    public Flight(int departureAirportId, int arrivalAirportId, LocalDateTime departureDateTime,
                  LocalDateTime arrivalDateTime, int aircraftId) {
        this.departureAirportId = departureAirportId;
        this.arrivalAirportId = arrivalAirportId;
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
        this.aircraftId = aircraftId;
    }

    public Flight(int flightNumber, int departureAirportId, int arrivalAirportId,
                  LocalDateTime departureDateTime, LocalDateTime arrivalDateTime, int aircraftId) {
        this.flightNumber = flightNumber;
        this.departureAirportId = departureAirportId;
        this.arrivalAirportId = arrivalAirportId;
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
        this.aircraftId = aircraftId;
    }


    public int getFlightNumber() { return flightNumber; }

    public int getDepartureAirportId() { return departureAirportId; }

    public int getArrivalAirportId() { return arrivalAirportId; }

    public LocalDateTime getDepartureDateTime() { return departureDateTime; }

    public LocalDateTime getArrivalDateTime() { return arrivalDateTime; }

    public int getAircraftId() { return aircraftId; }

    @Override
    public String toString() {
        return "Flight{" +
                "flightNumber=" + flightNumber +
                ", departureAirportId=" + departureAirportId +
                ", arrivalAirportId=" + arrivalAirportId +
                ", departureDateTime=" + departureDateTime +
                ", arrivalDateTime=" + arrivalDateTime +
                ", aircraftId=" + aircraftId +
                '}';
    }
}