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

public class Cluster {

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
     * Adds a set of List<TripRercord> onto the current cluster.
     * @param setOfTrips a list of trips which should be the core point's neighbours' neighbours
     */
    public void add(List<TripRecord> setOfTrips) {
        for (TripRecord newTrip : setOfTrips) {
            if (!this.cluster.contains(newTrip)) {
                this.cluster.add(newTrip);
            }
        }
    }

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
    public String toString() {
        return String.format("%s,%s,%s,%s\n", this.cluster.get(0).getLabel(), this.cluster.get(0).getPickup_Location().getLongitude(),
                this.cluster.get(0).getPickup_Location().getLatitude(), this.cluster.size());
    } // end of toString()


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
    }


} // end of Cluster.java
