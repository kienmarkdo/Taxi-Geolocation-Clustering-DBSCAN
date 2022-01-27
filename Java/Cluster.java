import java.util.List;

/**
 * Kien Do 300163370
 * CSI2520 - Paradigmes de programmation - Hiver 2022
 */

public class Cluster {

    // =========================== Attributes ==========================
    private int clusterID;
    private GPScoord thisCoord;
    private List<GPScoord> surroundingCoords;
    private int numOfSurroundingCoords;
    private boolean isNoise;

    // =========================== Methods ==========================

    // ==== constructors START ====
    public Cluster() {
        this.isNoise = false;
    }

    public Cluster(Cluster other) {
        this.clusterID = other.getClusterID();
        this.thisCoord = other.getThisCoord();
        this.surroundingCoords = other.getSurroundingCoords();
        this.numOfSurroundingCoords = other.getNumOfSurroundingCoords();
        this.isNoise = other.isNoise();
    }

    // ==== constructors END ====

    // ==== class methods START ====


    // ==== class methods END ====

    // ==== getters and setters START ====

    public int getClusterID() {
        return clusterID;
    }

    public void setClusterID(int clusterID) {
        this.clusterID = clusterID;
    }

    public GPScoord getThisCoord() {
        return thisCoord;
    }

    public void setThisCoord(GPScoord thisCoord) {
        this.thisCoord = thisCoord;
    }

    public List<GPScoord> getSurroundingCoords() {
        return surroundingCoords;
    }

    public void setSurroundingCoords(List<GPScoord> surroundingCoords) {
        this.surroundingCoords = surroundingCoords;
        this.numOfSurroundingCoords = this.surroundingCoords.size();
    }

    public int getNumOfSurroundingCoords() {
        return numOfSurroundingCoords;
    }

    public void setNumOfSurroundingCoords(int numOfSurroundingCoords) {
        this.numOfSurroundingCoords = numOfSurroundingCoords;
    }

    public boolean isNoise() {
        return isNoise;
    }

    public void setNoise(boolean noise) {
        isNoise = noise;
    }

    // ==== getters and setters END ====


}
