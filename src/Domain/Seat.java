package Domain;

public class Seat {
    private int id;
    private int row;
    private char position;
    private boolean extraLegRoom;
    private String passengerName;
    private int flightId;

    public Seat() {}

    public Seat(int row, char position, boolean extraLegRoom, String passengerName, int flightId) {
        this.row = row;
        this.position = position;
        this.extraLegRoom = extraLegRoom;
        this.passengerName = passengerName;
        this.flightId = flightId;
    }

    public Seat(int id, int row, char position, boolean extraLegRoom, String passengerName, int flightId) {
        this.id = id;
        this.row = row;
        this.position = position;
        this.extraLegRoom = extraLegRoom;
        this.passengerName = passengerName;
        this.flightId = flightId;
    }

    public int getId() { return id; }

    public int getRow() { return row; }

    public char getPosition() { return position; }

    public boolean isExtraLegRoom() { return extraLegRoom; }

    public String getPassengerName() { return passengerName; }

    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public int getFlightId() { return flightId; }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", row=" + row +
                ", position=" + position +
                ", extraLegRoom=" + extraLegRoom +
                ", passengerName='" + passengerName + '\'' +
                ", flightId=" + flightId +
                '}';
    }
}