package com.mycompany.mavenversion1;

public class UserStat {
    private int skierId;
    private int numLifts;
    private long totalVert;

    public UserStat(int skierId, int numLifts, long totalVert) {
        this.skierId = skierId;
        this.numLifts = numLifts;
        this.totalVert = totalVert;
    }

    public int getSkierId() {
        return skierId;
    }

    public void setSkierId(int skierId) {
        this.skierId = skierId;
    }

    public int getNumLifts() {
        return numLifts;
    }

    public void setNumLifts(int numLifts) {
        this.numLifts = numLifts;
    }

    public long getTotalVert() {
        return totalVert;
    }

    public void setTotalVert(long totalVert) {
        this.totalVert = totalVert;
    }
}
