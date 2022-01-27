/**
 * Kien Do 300163370
 * CSI2520 - Paradigmes de programmation - Hiver 2022
 */

public class GPScoord {

    // =========================== Attributes ==========================
    private double latitude;
    private double longitude;
    private boolean isNoise;

    // =========================== Methods ==========================

    // ==== constructors START ====
    public GPScoord() {
        this.latitude = -1;
        this.longitude = -1;
        this.isNoise = false;
    }


    public GPScoord(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.isNoise = false;
    }
    // ==== constructors END ====

    // ==== class methods START ====

    /**
     * Prints the LAT and LON coordinates of this GPScoord in a readable format.
     * For the classic format (LAT, LON), use toString().
     */
    public void printCoordinates() {
        System.out.printf("LAT: %f\nLON: %f\n", this.latitude, this.longitude);
    }

    /**
     * Calculates the distance from current GPScoord to other GPScoord.
     * @param otherCoord other GPScoord
     * @return (double) distance between this coord and other coord
     */
    public double calculateDistance(GPScoord otherCoord) {
        return Math.sqrt(
                Math.pow(this.latitude - otherCoord.getLatitude(), 2) +
                Math.pow(this.longitude - otherCoord.getLongitude(), 2)
        );
    }

    // ==== class methods END ====

    // ==== getters and setters START ====
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isNoise() {
        return isNoise;
    }

    public void setNoise(boolean noise) {
        isNoise = noise;
    }

    // ==== getters and setters END ====

    @Override
    public String toString() {
        return String.format("(%f, %f)", this.latitude, this.longitude);
    }
}
