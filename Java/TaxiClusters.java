import java.io.*;
import java.sql.Array;
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

    // CSV file indices
    static int PICKUP_DATETIME = 4;
    static int TRIP_DISTANCE = 7;
    static int START_LON = 8; // represents the column index of START_LON in the CSV file
    static int START_LAT = 9; // same as above...
    static int END_LON = 12;
    static int END_LAT = 13;

    // DBSCAN attributes
    static double EPS = 0.0001; // eps is a distance perimeter, like a radius
    static int MIN_PTS = 5; // minPts is the min points within EPS such that this radius counts as a cluster

    static ArrayList<TripRecord> TRIPS = new ArrayList<>();
    static ArrayList<Cluster> CLUSTERS = new ArrayList<>();

    // ================================  Attributes END  ================================


    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
        // TODO: Need to change all methods to non-static method then instantiate a TaxiCluster object in main()
        readDataIntoList();
        readToTripRecord();

        DBSCAN();
        for (Cluster cluster : CLUSTERS) {
            cluster.printCluster();
        }

        createAndWriteToCSV();


    }

    public static void DBSCAN() {
        int clusterCounter = 0;

        for (TripRecord currTrip: TRIPS) {

            if (!currTrip.getLabel().equals("undefined")) {
                continue;
            }

            ArrayList<TripRecord> neighbours = rangeQuery(currTrip);

            if (neighbours.size() < MIN_PTS) {
                currTrip.setLabel("noise");
                continue;
            }

            clusterCounter++;

            currTrip.setLabel(String.valueOf(clusterCounter));

            Cluster seedSet = new Cluster(); // Neighbours to expand
//            seedSet.setCoreTrip(currTrip);
//            seedSet.setSurroundingTrips(neighbours); // setSurroundingTrips remove the core point
//            List<TripRecord> surrSeedTrips = seedSet.getSurroundingTrips();

            seedSet.cluster.add(currTrip);
            seedSet.cluster.addAll(neighbours);

            for (int i = 0; i < seedSet.cluster.size(); i++) {
                if (seedSet.cluster.get(i).getLabel().equals("noise")) {
                    seedSet.cluster.get(i).setLabel(String.valueOf(clusterCounter));
                }
                if (!seedSet.cluster.get(i).getLabel().equals("undefined")) {
                    continue;
                }
                seedSet.cluster.get(i).setLabel(String.valueOf(clusterCounter));
                ArrayList<TripRecord> seedNeighbours = rangeQuery(seedSet.cluster.get(i));

                if (seedNeighbours.size() >= MIN_PTS) {
                    // seedSet.add(seedNeighbours);
                    seedSet.add(seedNeighbours);
                }

            } // end of for loop

//            for (int i = 0; i < surrSeedTrips.size(); i++) {
//                if (surrSeedTrips.get(i).getLabel().equals("noise")) {
//                    surrSeedTrips.get(i).setLabel(String.valueOf(clusterCounter));
//                }
//                if (!surrSeedTrips.get(i).getLabel().equals("undefined")) {
//                    continue;
//                }
//                surrSeedTrips.get(i).setLabel(String.valueOf(clusterCounter));
//                ArrayList<TripRecord> seedNeighbours = rangeQuery(surrSeedTrips.get(i));
//
//                if (seedNeighbours.size() >= MIN_PTS) {
//                    seedSet.add(seedNeighbours);
//                }
//
//            } // end of for loop

            CLUSTERS.add(seedSet);

        } // end of TripRecord for loop

    } // end of DBSCAN()

    /**
     * Scans the database and creates a list of neighbouring TripRecords WITHIN THE EPS RADIUS around the given TripRecord.
     * This method is used by DBSCAN.
     * @return a list of neighbouring TripRecords; returns an empty list if no neighbouring points exist within EPS.
     */
    public static ArrayList<TripRecord> rangeQuery(TripRecord thisTrip) {

        if (thisTrip == null) {
            throw new NullPointerException("ERROR : thisTrip parameter is Null");
        }

        ArrayList<TripRecord> neighbours = new ArrayList<>();

        // Scans all points in the database and compare it to the given coordinate
        for (TripRecord otherTrip: TRIPS) {
            // compute the distance between thisCoord and otherCoord to check EPS; EPS is a global variable
            if (thisTrip != otherTrip && thisTrip.calculateDistance(otherTrip) <= EPS) {
                // TODO: NOTE -- thisTrip != otherTrip is NOT the same as DBSCAN
                //  the actual DBSCAN algorithm includes thisTrip as a neighbouring point to be compared with MIN_PTS
                //  OTHER NOTE: With this current code, the current output's cluster count is the same as the Sample Output,
                //  but the Number of Points is slightly off.
                neighbours.add(otherTrip);
            }
        }

        return neighbours;
    } // end of rangeQuery()


    /**
     * Extract information relevant to TripRecord from List<List<String>> CSV_DATA_LIST.
     * Reads the CSV data from List<List<String>> CSV_DATA_LIST into an ArrayList of all TripRecords.
     */
    public static void readToTripRecord() {

        // i represents the row index in the CSV file
        for (int i = 1; i < CSV_DATA_LIST.size(); i++) {
            // extract information relevant to TripRecord individually, one at a time
            GPScoord currPickupCoord = new GPScoord(
                    Double.parseDouble(CSV_DATA_LIST.get(i).get(START_LAT)),
                    Double.parseDouble(CSV_DATA_LIST.get(i).get(START_LON))
            );
            GPScoord currDropoffCoord = new GPScoord(
                    Double.parseDouble(CSV_DATA_LIST.get(i).get(END_LAT)),
                    Double.parseDouble(CSV_DATA_LIST.get(i).get(END_LON))
            );

            String currPickupDateTime = CSV_DATA_LIST.get(i).get(PICKUP_DATETIME);
            float currDistance = Float.parseFloat(CSV_DATA_LIST.get(i).get(TRIP_DISTANCE));

            // add extracted information into a new object TripRecord then add the object to the list of all trips
            TripRecord thisTrip = new TripRecord(currPickupDateTime, currPickupCoord, currDropoffCoord, currDistance);
            TRIPS.add(thisTrip);
        }
    } // end of readToTripRecord()

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
            while ((currLine = br.readLine()) != null) {
                String[] values = currLine.split(COMMA_DELIMITER);
                CSV_DATA_LIST.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // end of readDataIntoList()

    /**
     * Prints the dataset after it has been transferred into a List, line by line.
     */
    public static void printListData() {
        System.out.println("File\n\n");

        for (int i = 0; i < CSV_DATA_LIST.size(); i++) {
            System.out.println(CSV_DATA_LIST.get(i));
        }
    } // end of printListData()

    /**
     * Creates a CSV file and write CLUSTERS to it.
     */
    public static void createAndWriteToCSV() {
        try (PrintWriter writer = new PrintWriter("test_" + EPS + "_" + MIN_PTS + ".csv")) {

            StringBuilder sb = new StringBuilder("Cluster_ID,Longitude,Latitude,Number_Of_Points\n");

            for (int i = 0; i < CLUSTERS.size(); i++) {
                sb.append(CLUSTERS.get(i).toString());
            }

            writer.write(sb.toString());

            System.out.println("done!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }




}
