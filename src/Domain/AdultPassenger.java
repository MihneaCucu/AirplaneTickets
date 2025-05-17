package Domain;

import Utils.CheckedInBaggage;
import Utils.Gender;

public class AdultPassenger extends Passenger {

    private Gender gender;
    private boolean specialAssistance;
    private CheckedInBaggage checkedInBaggage;
    private boolean CabinBaggage;

    public AdultPassenger(int id, String firstName, String lastName, Gender gender,
                          boolean specialAssistance, CheckedInBaggage checkedInBaggage, boolean cabinBaggage) {
        super(id, firstName, lastName);
        this.gender = gender;
        this.specialAssistance = specialAssistance;
        this.checkedInBaggage = checkedInBaggage;
        this.CabinBaggage = cabinBaggage;

    }

    public Gender getGender() {
        return gender;
    }

    public boolean isSpecialAssistance() {
        return specialAssistance;
    }

    public CheckedInBaggage getCheckedInBaggage() {
        return checkedInBaggage;
    }

    public boolean isCabinBaggage() {
        return CabinBaggage;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setSpecialAssistance(boolean specialAssistance) {
        this.specialAssistance = specialAssistance;
    }

    public void setCheckedInBaggage(CheckedInBaggage checkedInBaggage) {
        this.checkedInBaggage = checkedInBaggage;
    }

    public void setCabinBaggage(boolean cabinBaggage) {
        CabinBaggage = cabinBaggage;
    }
}
