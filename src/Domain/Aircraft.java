package Domain;

import Utils.AircraftType;


public class Aircraft {
    private int id;
    private AircraftType aircraftType;
    private int totalSeats;

    public Aircraft(int id, AircraftType aircraftType, int totalSeats) {
        this.id = id;
        this.aircraftType = aircraftType;
        this.totalSeats = totalSeats;
    }

    public int getId() {
        return id;
    }

    public AircraftType getAircraftType() {
        return aircraftType;
    }

    public int getTotalSeats() {
        return totalSeats;
    }


}