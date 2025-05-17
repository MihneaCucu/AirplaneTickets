package Domain;

public abstract class Passenger {
    private static int idCounter = 1;
    private final int id;
    private String firstName;
    private String lastName;

    public Passenger(String firstName, String lastName) {
        this.id = idCounter++;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Passenger(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


}
