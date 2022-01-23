/**
 * Kien Do 300163370
 * CSI2520 - Paradigmes de programmation - Hiver 2022
 */

public class GPScoord {

    // =========================== Attributes ==========================
    private double longitude;
    private double latitude;

    // =========================== Methods ==========================

    // ==== constructors START ====
    public GPScoord() {

    }


    public GPScoord(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
    // ==== constructors END ====

    // ==== class methods START ====
    public void printCoordinate() {
        System.out.printf("LON: %f\nLAT: %f\n", this.longitude, this.latitude);
    }

    // ==== class methods END ====

    // ==== getters and setters START ====
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    // ==== getters and setters END ====
}
