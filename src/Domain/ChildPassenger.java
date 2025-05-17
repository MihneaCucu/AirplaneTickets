package Domain;

import java.time.LocalDate;
import Utils.CheckedInBaggage;
import Utils.Gender;

public class ChildPassenger extends AdultPassenger {

    private LocalDate dateOfBirth;

    public ChildPassenger(int id, String firstName, String lastName, Gender gender,
                          boolean specialAssistance, CheckedInBaggage checkedInBaggage, boolean cabinBaggage, LocalDate dateOfBirth) {
        super(id, firstName, lastName, gender, specialAssistance, checkedInBaggage, cabinBaggage);
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
