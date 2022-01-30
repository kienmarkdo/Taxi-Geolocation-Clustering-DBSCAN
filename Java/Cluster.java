import java.util.ArrayList;
import java.util.List;

/**
 * Kien Do 300163370
 * CSI2520 - Paradigmes de programmation - Hiver 2022
 */

public class Cluster {

    // =========================== Attributes ==========================
    //private TripRecord coreTrip;
    //private List<TripRecord> surroundingTrips;
    public List<TripRecord> cluster; // the 0th index is the coreTrip

    // =========================== Methods ==========================

    // ==== constructors START ====
    public Cluster() {
//        this.coreTrip = new TripRecord();
//        this.surroundingTrips = new ArrayList<>();
        cluster = new ArrayList<>();
    }

    // ==== constructors END ====

    // ==== class methods START ====

    public void printCluster() {
//        System.out.printf("" +
//                "Cluster ID: %3s          " +
//                "Longitude: %10f          " +
//                "Latitude: %10f           " +
//                "Number of points: %3d\n",
//                this.coreTrip.getLabel(), this.coreTrip.getPickup_Location().getLongitude(),
//                this.coreTrip.getPickup_Location().getLatitude(), this.size()
//        );

            System.out.printf("" +
                "Cluster ID: %3s          " +
                "Longitude: %10f          " +
                "Latitude: %10f           " +
                "Number of points: %3d\n",
                this.cluster.get(0).getLabel(), this.cluster.get(0).getPickup_Location().getLongitude(),
                this.cluster.get(0).getPickup_Location().getLatitude(), this.cluster.size()
        );
    }

    public void add(List<TripRecord> setOfTrips) {
        for (TripRecord newTrip: setOfTrips) {
            if (!this.cluster.contains(newTrip)) {
                this.cluster.add(newTrip);
            }
        }
    }

//    public void add(List<TripRecord> setOfTrips) {
//        for (TripRecord newTrip: setOfTrips) {
//            if (!this.surroundingTrips.contains(newTrip)) {
//                this.surroundingTrips.add(newTrip);
//            }
//        }
//    }
//
//    public int size() {
//        return this.surroundingTrips.size();
//    }
//
//    // ==== class methods END ====
//
//    // ==== getters and setters START ====
//
//    public TripRecord getCoreTrip() {
//        return coreTrip;
//    }
//
//    public void setCoreTrip(TripRecord coreTrip) {
//        this.coreTrip = coreTrip;
//    }
//
//    public List<TripRecord> getSurroundingTrips() {
//        return surroundingTrips;
//    }
//
//    public void setSurroundingTrips(List<TripRecord> surroundingTrips) {
//        // this.surroundingTrips = surroundingTrips;
//        for (int i = 0; i < surroundingTrips.size(); i++) {
//            this.surroundingTrips.add(surroundingTrips.get(i));
//        }
//        this.surroundingTrips.remove(this.coreTrip);
//    }
//
//
//    // ==== getters and setters END ====
//
//    /**
//     * toString method
//     * @return Returns a string of Cluster in CSV format with the comma being the separator.
//     */
//    public String toString() {
//        return String.format("%s,%s,%s,%s\n", this.coreTrip.getLabel(), this.coreTrip.getPickup_Location().getLongitude(),
//                this.coreTrip.getPickup_Location().getLatitude(), this.size());
//    }


        public String toString() {
            return String.format("%s,%s,%s,%s\n", this.cluster.get(0).getLabel(), this.cluster.get(0).getPickup_Location().getLongitude(),
                    this.cluster.get(0).getPickup_Location().getLatitude(), this.cluster.size());
        }


}
