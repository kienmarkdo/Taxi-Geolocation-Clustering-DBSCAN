import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Kien Do 300163370
 * CSI2520 - Paradigmes de programmation - Hiver 2022
 *
 * Cluster class representing ONE cluster of TripRecords.
 */

public class Cluster implements Comparable<Cluster> {

    // =========================== Attributes ==========================

    private List<TripRecord> cluster; // the 0th index is the core trip/core point of the cluster

    // =========================== Methods ==========================

    // ==== constructors START ====

    public Cluster() {
        cluster = new ArrayList<>();
    }

    // ==== constructors END ====

    // ==== class methods START ====

    /**
     * Prints the cluster in a table format with spaces in-between columns for easy readability.
     */
    public void printCluster() {
        System.out.printf("" +
                        "Cluster ID: %3s          " +
                        "Longitude: %10f          " +
                        "Latitude: %10f           " +
                        "Number of points: %3d\n",
                this.cluster.get(0).getLabel(), this.cluster.get(0).getPickup_Location().getLongitude(),
                this.cluster.get(0).getPickup_Location().getLatitude(), this.cluster.size()
        );
    }

    /**
     * Adds a set of List<TripRecord> onto the current cluster.
     * @param setOfTrips a list of trips which should be the core point's neighbours' neighbours
     */
    public void add(List<TripRecord> setOfTrips) {
        for (TripRecord newTrip : setOfTrips) {
            if (!this.cluster.contains(newTrip)) {
                this.cluster.add(newTrip);
            }
        }
    }

    /**
     * Calculates the average of all the pickup location coordinates in this cluster.
     * Used for the version that produces one single CSV file containing all clusters.
     * @return the pickup location coordinate which is the average of all the pickup location coordinates within this cluster
     */
    public GPScoord averagePickupCoord() {
        GPScoord average;
        double avgLat = 0;
        double avgLon = 0;
        for (int i = 0; i < cluster.size(); i++) {
            avgLat += cluster.get(i).getPickup_Location().getLatitude();
            avgLon += cluster.get(i).getPickup_Location().getLongitude();
        }

        avgLat /= cluster.size();
        avgLon /= cluster.size();

        average = new GPScoord(avgLat, avgLon);

        return average;
    }

    /**
     * Produces the CSV file containing the Cluster ID, the Longitude and Latitude of this cluster (average of all pick up points),
     * and the number of points in this cluster. The CSV file is named according to the cluster ID.
     */
    public void createCSV() {
        try (PrintWriter writer = new PrintWriter("cluster_" + this.getCluster().get(0).getLabel() + ".csv")) {

            StringBuilder sb = new StringBuilder("Cluster_ID,Longitude,Latitude,Number_Of_Points\n");

            for (int i = 0; i < this.getCluster().size(); i++) {
                sb.append(
                        String.format("%s,%s,%s,%s\n", this.cluster.get(i).getLabel(), this.cluster.get(i).getPickup_Location().getLongitude(),
                                this.cluster.get(i).getPickup_Location().getLatitude(), this.cluster.size())
                );
            }

            writer.write(sb.toString());

            System.out.println("done!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    } // createCSV()

    // ==== class methods END ====

    // ==== getters and setters START ====

    public List<TripRecord> getCluster() {
        return cluster;
    }

    public void setCluster(List<TripRecord> cluster) {
        this.cluster = cluster;
    }


    // ==== getters and setters END ====

    /**
     * toString() method returns the object in a string format.
     *
     * @return Returns a string of Cluster in CSV format with the comma being the separator.
     */
    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s\n", this.cluster.get(0).getLabel(), averagePickupCoord().getLongitude(),
                averagePickupCoord().getLatitude(), this.cluster.size());
    } // end of toString()


    /**
     * compareTo method for Cluster; a method of the interface Comparable
     * @param other other Cluster object
     * @return 1 if this cluster's size < other cluster's size; -1 if this cluster's size < other cluster's size; 0 if they are equal
     */
    @Override
    public int compareTo(Cluster other) {
        if (other == null) {
            throw new NullPointerException("ERROR : CompareTo() method in Cluster.java received a null value for other cluster.");
        }

        if (this.getCluster().size() > other.getCluster().size()) {
            return 1;
        } else if (this.getCluster().size() < other.getCluster().size()) {
            return -1;
        } else {
            return 0;
        }
    } // end of compareTo()

} // end of Cluster.java
