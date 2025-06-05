package Domain;

import Utils.AircraftType;

public class PassengerAircraft extends Aircraft {
    private int totalSeats;

    public PassengerAircraft(int id, AircraftType aircraftType, int totalSeats) {
        super(id, aircraftType);
        this.totalSeats = totalSeats;
    }

    public int getTotalSeats() {
        return totalSeats;
    }
}
