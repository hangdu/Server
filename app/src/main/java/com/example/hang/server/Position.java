package com.example.hang.server;

/**
 * Created by hang on 4/17/18.
 */

public class Position {
    private String label;
    private double RSSI;
    public Position(String label, double RSSI) {
        this.label = label;
        this.RSSI = RSSI;
    }

    public String getLabel() {
        return label;
    }

    public double getRSSI() {
        return RSSI;
    }
}