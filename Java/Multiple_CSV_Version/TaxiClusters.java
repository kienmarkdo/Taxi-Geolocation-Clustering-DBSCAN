import java.io.*;
import java.util.*;

/**
 * Kien Do 300163370
 * CSI2520 - Paradigmes de programmation - Hiver 2022
 *
 * TaxiClusters class is the main class that runs DBSCAN and produces the CSV output file of clusters.
 * This program produces as output the list of clusters contained in a CSV file specifying, for each cluster,
 * its position (average value of the GPS coordinates of its point set) and the number of points it contains.
 * The outlier points are discarded.
 *
 * This version of the program produces MULTIPLE CSV files, a new CSV file for every cluster.
 */

public class TaxiClusters {

    // ================================  Attributes START  ================================
    // CSV file's required information before it can be processed
    static String COMMA_DELIMITER = ","; // defining the comma delimiter of the CSV file
    static String DATASET_NAME = "yellow_tripdata_2009-01-15_1hour_clean.csv";
    static List<List<String>> CSV_DATA_LIST = new ArrayList<>(); // must be initialized with readDataIntoList()

    // column indices of the relevant information in the CSV file
    static int PICKUP_DATETIME = 4;
    static int TRIP_DISTANCE = 7;
    static int START_LON = 8; // represents the column index of START_LON in the CSV file
    static int START_LAT = 9; // same as above...
    static int END_LON = 12;
    static int END_LAT = 13;

    // DBSCAN attributes
    static double EPS = 0.0003; // eps is a distance perimeter, like a radius
    static int MIN_PTS = 5; // minPts is the min points within EPS such that this radius counts as a cluster

    // a list of all TripRecords and Clusters; CLUSTERS will be used to output the CSV file containing all the clusters
    static ArrayList<TripRecord> TRIPS = new ArrayList<>();
    static ArrayList<Cluster> CLUSTERS = new ArrayList<>();
    // ================================  Attributes END  ================================

    /**
     * Main method
     *
     * @param args
     */
    public static void main(String[] args) {
        TaxiClusters taxiClusters = new TaxiClusters();
        taxiClusters.readDataIntoList();
        taxiClusters.readToTripRecord();

        taxiClusters.DBSCAN();
//        for (Cluster cluster : CLUSTERS) {
//            cluster.printCluster();
//        }

//        // NOTE : https://stackoverflow.com/questions/34910841/difference-between-collections-sortlist-and-list-sortcomparator
//        CLUSTERS.sort(Collections.reverseOrder()); // requires Java 8
//        taxiClusters.createAndWriteResultsToCSV();
    }

    /**
     * DBSCAN algorithm used to cluster all the TripRecords together while discarding all TripRecords considered as noise.
     * Does not take EPS and MIN_PTS as parameters because they are global variables.
     */
    public void DBSCAN() {
        int clusterCounter = 0;

        for (TripRecord currTrip : TRIPS) {

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
            seedSet.getCluster().add(currTrip);
            seedSet.getCluster().addAll(neighbours);

            for (int i = 0; i < seedSet.getCluster().size(); i++) {
                if (seedSet.getCluster().get(i).getLabel().equals("noise")) {
                    seedSet.getCluster().get(i).setLabel(String.valueOf(clusterCounter));
                }
                if (!seedSet.getCluster().get(i).getLabel().equals("undefined")) {
                    continue;
                }
                seedSet.getCluster().get(i).setLabel(String.valueOf(clusterCounter));
                ArrayList<TripRecord> seedNeighbours = rangeQuery(seedSet.getCluster().get(i));

                if (seedNeighbours.size() >= MIN_PTS) {
                    seedSet.add(seedNeighbours); // does not add duplicate neighbours
                }
            } // end of for loop

            // CLUSTERS.add(seedSet); // single CSV file version (each cluster's position is the average of all of its starting points)
            seedSet.createCSV(); // multiple CSV file version (one CSV file per cluster; shows all neighbours' starting points)

        } // end of TripRecord for loop

    } // end of DBSCAN()


    /**
     * Scans the database and creates a list of neighbouring TripRecords WITHIN THE DEFINED EPS RADIUS around the given TripRecord.
     * This method is used by DBSCAN.
     *
     * @param thisTrip this core trip / core point is required to determine its own neighbouring trips
     * @return a list of neighbouring TripRecords; returns an empty list if no neighbouring points exist within EPS.
     */
    public ArrayList<TripRecord> rangeQuery(TripRecord thisTrip) {

        if (thisTrip == null) {
            throw new NullPointerException("ERROR : TripRecord parameter is NULL");
        }

        ArrayList<TripRecord> neighbours = new ArrayList<>();

        // Scans all points in the database and compares those points to the given TripRecord's starting coordinate
        for (TripRecord otherTrip : TRIPS) {
            // compute the distance between thisTrip and otherTrip; compare that distance to EPS; EPS is a global variable
            if (thisTrip.calculateDistance(otherTrip) <= EPS) {
                neighbours.add(otherTrip);
            }
        }

        return neighbours;
    } // end of rangeQuery()


    /**
     * Reads the CSV data from List<List<String>> CSV_DATA_LIST into an ArrayList of all TripRecords.
     * Extracts information relevant to TripRecord from List<List<String>> CSV_DATA_LIST.
     */
    public void readToTripRecord() {

        // int i represents the row index in the CSV file; starts at 1 because the first row is not actual data
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

            // add extracted information into a new TripRecord object then add the object to the list of all trips
            TripRecord thisTrip = new TripRecord(currPickupDateTime, currPickupCoord, currDropoffCoord, currDistance);
            TRIPS.add(thisTrip);
        }
    } // end of readToTripRecord()

    /**
     * Puts the data from the CSV file into a List.
     * The dataset's name and the List to which the information will be inputted are global variables.
     */
    public void readDataIntoList() {
        // stores the CSV file in a list of lists so that it can be processed
        try {
            BufferedReader br = new BufferedReader(new FileReader(DATASET_NAME));
            String currLine;
            while ((currLine = br.readLine()) != null) {
                String[] values = currLine.split(COMMA_DELIMITER); // splits the values by their comma delimiter
                CSV_DATA_LIST.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // end of readDataIntoList()

    /**
     * Prints the entire dataset in its raw text-file format AFTER it has been transferred into a List.
     */
    public void printListData() {
        System.out.println("File\n\n");

        for (int i = 0; i < CSV_DATA_LIST.size(); i++) {
            System.out.println(CSV_DATA_LIST.get(i));
        }
    } // end of printListData()

    /**
     * Creates a CSV file and write CLUSTERS to it. This method essentially outputs clusters after the input data
     *  has been processed by DBSCAN and populated into an object that stores all the clusters.
     */
    public void createAndWriteResultsToCSV() {
        // creates the CSV file's name according to its EPS and MIN_PTS values
        try (PrintWriter writer = new PrintWriter("output_sample_SORTED_" + EPS + "_" + MIN_PTS + ".csv")) {

            StringBuilder sb = new StringBuilder("Cluster_ID,Longitude,Latitude,Number_Of_Points\n");

            for (int i = 0; i < CLUSTERS.size(); i++) {
                sb.append(CLUSTERS.get(i).toString());
            }

            writer.write(sb.toString());

            System.out.println("Create and write output to CSV file done!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    } // end of createAndWriteToCSV()


} // end of TaxiClusters.java
