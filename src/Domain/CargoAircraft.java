package Domain;

import Utils.AircraftType;

public class CargoAircraft extends Aircraft {
    private int cargoCapacity; // capacitate marfa in tone

    public CargoAircraft(int id, AircraftType aircraftType, int cargoCapacity) {
        super(id, aircraftType);
        this.cargoCapacity = cargoCapacity;
    }

    public int getCargoCapacity() {
        return cargoCapacity;
    }

    public void setCargoCapacity(int cargoCapacity) {
        this.cargoCapacity = cargoCapacity;
    }
}
