package Domain;

import Utils.AircraftType;


public class Aircraft {
    private int id;
    private AircraftType aircraftType;

    public Aircraft(int id, AircraftType aircraftType) {
        this.id = id;
        this.aircraftType = aircraftType;
    }

    public int getId() {
        return id;
    }

    public AircraftType getAircraftType() {
        return aircraftType;
    }


}