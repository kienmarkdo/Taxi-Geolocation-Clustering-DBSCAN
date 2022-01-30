/**
 * Kien Do 300163370
 * CSI2520 - Paradigmes de programmation - Hiver 2022
 */

public class TripRecord {

    // =========================== Attributes ==========================
    private String pickup_DateTime;
    private GPScoord pickup_Location;
    private GPScoord dropoff_Location;
    private float trip_Distance;
    private String label;

    // =========================== Methods ==========================


    // ==== constructors START ====

    public TripRecord() {
        this.label = "undefined";
    }

    public TripRecord(String pickup_DateTime, GPScoord pickup_Location, GPScoord dropoff_Location, float trip_Distance) {
        this.pickup_DateTime = pickup_DateTime;
        this.pickup_Location = pickup_Location;
        this.dropoff_Location = dropoff_Location;
        this.trip_Distance = trip_Distance;
        this.label = "undefined";
    }

    // ==== constructors END ====

    // ==== class methods START ====

    public void printTripRecord() {
        System.out.printf(
                "Pickup_Datetime  : %s\n" +
                "Pickup_Location  : %s\n" +
                "Dropoff_Location : %s\n" +
                "Trip_Distance    : %f\n",
                this.pickup_DateTime, this.pickup_Location, this.dropoff_Location, this.trip_Distance
        );
    }

    /**
     * Calculates the distance from current pickup_location to other pickup_location.
     * @param otherTrip other trip
     * @return (float) distance between this starting coord and other starting coord
     */
    public double calculateDistance(TripRecord otherTrip) {
        return Math.sqrt(
                Math.pow(this.pickup_Location.getLatitude() - otherTrip.getPickup_Location().getLatitude(), 2) +
                        Math.pow(this.pickup_Location.getLongitude() - otherTrip.getPickup_Location().getLongitude(), 2)
        );
    }

    // ==== class methods END ====

    // ==== getters and setters START ====

    public String getPickup_DateTime() {
        return pickup_DateTime;
    }

    public void setPickup_DateTime(String pickup_DateTime) {
        this.pickup_DateTime = pickup_DateTime;
    }

    public GPScoord getPickup_Location() {
        return pickup_Location;
    }

    public void setPickup_Location(GPScoord pickup_Location) {
        this.pickup_Location = pickup_Location;
    }

    public GPScoord getDropoff_Location() {
        return dropoff_Location;
    }

    public void setDropoff_Location(GPScoord dropoff_Location) {
        this.dropoff_Location = dropoff_Location;
    }

    public float getTrip_Distance() {
        return trip_Distance;
    }

    public void setTrip_Distance(float trip_Distance) {
        this.trip_Distance = trip_Distance;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    // ==== getters and setters END ====

    @Override
    public String toString() {
        return this.pickup_Location.toString();
    }
}
