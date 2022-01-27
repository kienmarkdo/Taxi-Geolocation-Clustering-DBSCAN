import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Kien Do 300163370
 * CSI2520 - Paradigmes de programmation - Hiver 2022
 */

public class TaxiClusters {

    // eps is a distance perimeter, like a radius
    // minPts is the min points within eps such that this radius counts as a cluster

    /**
     * Thinking of making a key:value data structure.
     * GPS is the unique key, representing the starting point, ~~# of points within the radius is the value~~.
     *  the coordinates of the other points within the radius is the value
     */

//    DBSCAN(DB, distFunc, eps, minPts) {
//        C := 0 /* Cluster counter */
//        for each point P in database DB {
//            if label(P) ≠ undefined then continue /* Previously processed in inner loop */
//                    Neighbors N := RangeQuery(DB, distFunc, P, eps) /* Find neighbors */
//            if |N| < minPts then { /* Density check */
//                label(P) := Noise /* Label as Noise */
//                continue
//            }
//            C := C + 1 /* next cluster label */
//            label(P) := C /* Label initial point */
//            SeedSet S := N \ {P} /* Neighbors to expand */
//            for each point Q in S { /* Process every seed point Q */
//                if label(Q) = Noise then label(Q) := C /* Change Noise to border point */
//                if label(Q) ≠ undefined then continue /* Previously processed */
//                        label(Q) := C /* Label neighbor */
//                Neighbors N := RangeQuery(DB, distFunc, Q, eps) /* Find neighbors */
//                if |N| ≥ minPts then { /* Density check (if Q is a core point) */
//                    S := S ∪ N /* Add new neighbors to seed set */
//                }
//            }
//        }
//    }
//    RangeQuery(DB, distFunc, Q, eps) {
//        Neighbors N := empty list
//        for each point P in database DB { /* Scan all points in the database */
//            if distFunc(Q, P) ≤ eps then { /* Compute distance and check epsilon */
//                N := N ∪ {P} /* Add to result */
//            }
//        }
//        return N
//    }
//    /* Reference: https://en.wikipedia.org/wiki/DBSCAN */

    static String COMMA_DELIMITER = ","; // defining the comma delimiter of the CSV file
    static String DATASET_NAME = "yellow_tripdata_2009-01-15_1hour_clean.csv";
    static List<List<String>> CSV_DATA_LIST = new ArrayList<>(); // must be initialized with readDataIntoList()

    static int START_LON = 8; // represents the column index of START_LON in the CSV file
    static int START_LAT = 9; // same as above...
    static int END_LON = 12;
    static int END_LAT = 13;

    static int PICKUP_DATETIME = 4;
    static int TRIP_DISTANCE = 7;

    // DBSCAN attributes
    static double EPS = 0.01;
    static int MIN_PTS = 5;

    static ArrayList<GPScoord> allCoords;


    public static void main(String[] args) {
        readDataIntoList();
        //printListData();
        addCoordsToArrayList();

//        System.out.println("======================");
//
        GPScoord firstPickupCoord = new GPScoord(
                Double.parseDouble(CSV_DATA_LIST.get(1).get(START_LAT)),
                Double.parseDouble(CSV_DATA_LIST.get(1).get(START_LON))
        );
//        System.out.println("First pick up coord");
//        firstPickUpCoord.printCoordinates();
//
//        System.out.println("======================");
//
//        GPScoord firstDropoffCoord = new GPScoord(
//                Double.parseDouble(CSV_DATA_LIST.get(1).get(END_LAT)),
//                Double.parseDouble(CSV_DATA_LIST.get(1).get(END_LON))
//        );
        GPScoord secondPickupCoord = new GPScoord(
                Double.parseDouble(CSV_DATA_LIST.get(2).get(START_LAT)),
                Double.parseDouble(CSV_DATA_LIST.get(2).get(START_LON))
        );

        // calculate the distance between current GPScoord and another GPScoord
        double distance = firstPickupCoord.calculateDistance(secondPickupCoord);
        System.out.println("Distance b/w first pickup and second pickup coord:");
        System.out.println(distance);



//        System.out.println("First drop off coord");
//        firstDropoffCoord.printCoordinates();
//
//        System.out.println("======================");
//
//        TripRecord firstTrip = new TripRecord(
//                CSV_DATA_LIST.get(1).get(PICKUP_DATETIME),
//                firstPickUpCoord,
//                firstDropoffCoord,
//                Float.parseFloat(CSV_DATA_LIST.get(1).get(TRIP_DISTANCE))
//        );
//        System.out.println("First trip record");
//        firstTrip.printTripRecord();

        // get surrounding coordinates
        // calculate and see if there are other coordinates within the radius
        // add all surrounding coordinates to this firstCoord's cluster




    }

    public static void addCoordsToArrayList() {
        allCoords = new ArrayList<GPScoord>();

        // i represents the row index in the CSV file
        for (int i = 1; i < CSV_DATA_LIST.size(); i++) {
            GPScoord currCoord = new GPScoord(
                    Double.parseDouble(CSV_DATA_LIST.get(i).get(START_LAT)),
                    Double.parseDouble(CSV_DATA_LIST.get(i).get(START_LON))
            );
            allCoords.add(currCoord);
        }

//        System.out.println("All coords:");
//        System.out.println(allCoords);
    }

    /**
     * Puts the data from the CSV file into a List.
     * The dataset's name and the List to which the information will be inputted are global variables.
     */
    public static void readDataIntoList() {
        // store the CSV file in a list of lists
        // outer lister represents the CSV file, inner list represents each line of the CSV file
        try {
            BufferedReader br = new BufferedReader(new FileReader(DATASET_NAME));
            String currLine;
            int i = 0;
            while ((currLine = br.readLine()) != null && i < 10) {
                ++i;
                String[] values = currLine.split(COMMA_DELIMITER);
                CSV_DATA_LIST.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints the dataset after it has been transferred into a List, line by line.
     */
    public static void printListData() {
        System.out.println("File\n\n");

        for (int i = 0; i < CSV_DATA_LIST.size(); i++) {
            System.out.println(CSV_DATA_LIST.get(i));
        }
    }

//    private List<String> getRecordFromLine(String line) {
//        List<String> values = new ArrayList<String>();
//        try (Scanner rowScanner = new Scanner(line)) {
//            rowScanner.useDelimiter(COMMA_DELIMITER);
//            while (rowScanner.hasNext()) {
//                values.add(rowScanner.next());
//            }
//        }
//        return values;
//    }




}
