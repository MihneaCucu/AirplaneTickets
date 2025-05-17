package Utils;

public enum SeatPosition {
    A, B, C, D, E, F;

    public static SeatPosition fromString(String value) {
        try {
            return SeatPosition.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid SeatPosition: " + value);
        }
    }
}