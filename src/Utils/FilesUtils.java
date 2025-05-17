/*package Utils;

import Domain.Flight;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FilesUtils {
    public static void writeFlights(String fileName, List<Flight> flights) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true)); // true pentru append
        for (Flight flight : flights) {
            String line = flight.getFlightNumber() + "," +
                    flight.getDepartureLocation() + "," +
                    flight.getArrivalLocation() + "," +
                    flight.getDepartureDateTime() + "," +
                    flight.getArrivalDateTime();
            bw.write(line);
            bw.newLine();
        }
        bw.close();
    }
}*/