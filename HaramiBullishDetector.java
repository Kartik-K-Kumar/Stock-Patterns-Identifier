import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HaramiBullishDetector {

    public static void main(String[] args) {
        String csvFile = "DataPacket.csv"; 
        String line;
        String splitBy = ",";
        String lastDetectedDate = ""; 

        double prevOpen = 0, prevClose = 0, prevHigh = 0, prevLow = 0;
        double currOpen, currClose, currHigh, currLow;
        String prevDate = null;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
         
            br.readLine();

        
            line = br.readLine();
            if (line == null || line.trim().isEmpty()) {
                System.out.println("No data available in the file.");
                return;
            }

            String[] prevRow = line.split(splitBy);
            if (prevRow.length < 5) {
                System.out.println("Invalid data format in the first line.");
                return;
            }

            prevDate = prevRow[0];
            prevOpen = Double.parseDouble(prevRow[1]);
            prevClose = Double.parseDouble(prevRow[2]);
            prevHigh = Double.parseDouble(prevRow[3]);
            prevLow = Double.parseDouble(prevRow[4]);

        
            while ((line = br.readLine()) != null) {
                
                if (line.trim().isEmpty()) continue;

                String[] currentRow = line.split(splitBy);
                if (currentRow.length < 5) {
                    System.out.println("Skipping invalid row: " + line);
                    continue;
                }

                try {
                 
                    String currDate = currentRow[0];
                    currOpen = Double.parseDouble(currentRow[1]);
                    currClose = Double.parseDouble(currentRow[2]);
                    currHigh = Double.parseDouble(currentRow[3]);
                    currLow = Double.parseDouble(currentRow[4]);

                    
                    if (isHaramiBullish(prevOpen, prevClose, prevHigh, prevLow, currOpen, currClose, currHigh, currLow)) {
                        
                        if (!prevDate.equals(lastDetectedDate)) {
                            System.out.println("Harami Bullish pattern detected on: " + prevDate);
                            lastDetectedDate = prevDate; 
                        }
                    }

                    
                    prevDate = currDate;
                    prevOpen = currOpen;
                    prevClose = currClose;
                    prevHigh = currHigh;
                    prevLow = currLow;

                } catch (NumberFormatException e) {
                    System.out.println("Skipping row with invalid number format: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    
    public static boolean isHaramiBullish(
            double prevOpen, double prevClose, double prevHigh, double prevLow,
            double currOpen, double currClose, double currHigh, double currLow) {

        
        boolean prevRedCandle = prevOpen > prevClose;

        
        boolean currGreenCandle = currClose > currOpen;

        
        boolean isEngulfed = currHigh <= prevHigh && currLow >= prevLow;

        return prevRedCandle && currGreenCandle && isEngulfed;
    }
}
