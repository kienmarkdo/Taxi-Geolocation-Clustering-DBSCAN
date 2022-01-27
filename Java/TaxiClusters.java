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

    // ================================  Attributes START  ================================
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
    static double EPS = 0.01; // eps is a distance perimeter, like a radius
    static int MIN_PTS = 5; // minPts is the min points within EPS such that this radius counts as a cluster

    static ArrayList<GPScoord> ALL_START_COORDS;

    // ================================  Attributes END  ================================


    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
        readDataIntoList();
        addStartCoordsToArrayList();


    }

    public static void DBSCAN() {
        int clusterCounter = 0;

        for (GPScoord currCoord: ALL_START_COORDS) {
            if (currCoord == null) {
                continue;
            }

            List<GPScoord> neighbours = rangeQuery(currCoord);

            if (neighbours.size() < MIN_PTS) {
                currCoord.setNoise(true);
            }

            clusterCounter++;

            int initialID = clusterCounter;

            Cluster seedSet = new Cluster();
            seedSet.setSurroundingCoords(neighbours);

            for (GPScoord seedPoint: seedSet.getSurroundingCoords()) {
                if (seedPoint.isNoise()) {
                    seedPoint.;
                }
            }


        }
    } // end of DBSCAN()

    /**
     * Scans the database and creates a list of neighbouring GPScoords WITHIN THE EPS RADIUS around the given GPScoord.
     * This method is used by DBSCAN.
     * @return a list of neighbouring GPScoords; returns an empty list if no neighbouring points exist within EPS.
     */
    public static List<GPScoord> rangeQuery(GPScoord thisCoord) {
        List<GPScoord> neighbours = new ArrayList<GPScoord>();

        // Scans all points in the database and compare it to the given coordinate
        for (GPScoord otherCoord: ALL_START_COORDS) {
            // compute the distance between thisCoord and otherCoord to check EPS; EPS is a global variable
            if (thisCoord.calculateDistance(otherCoord) <= EPS) {
                neighbours.add(otherCoord);
            }
        }
        return neighbours;
    } // end of rangeQuery


    /**
     * Adds all the start coords from the List that represents the CSV file into an ArrayList.
     * Extract START_LAT and START_LON from CSV_DATA_LIST and puts it into an ArrayList of GPScoords.
     */
    public static void addStartCoordsToArrayList() {
        ALL_START_COORDS = new ArrayList<GPScoord>();

        // i represents the row index in the CSV file
        for (int i = 1; i < CSV_DATA_LIST.size(); i++) {
            GPScoord currCoord = new GPScoord(
                    Double.parseDouble(CSV_DATA_LIST.get(i).get(START_LAT)),
                    Double.parseDouble(CSV_DATA_LIST.get(i).get(START_LON))
            );
            ALL_START_COORDS.add(currCoord);
        }
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




}
