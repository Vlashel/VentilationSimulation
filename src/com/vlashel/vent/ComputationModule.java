package com.vlashel.vent;

/**
 * @author Vlashel
 * @version 1.0
 * @since 27.04.2015.
 */
public class ComputationModule {
    private double totalTime;
    private int steps;
    private double[] roomATemperatures;
    private double[] roomBTemperatures;
    private double Q;
    private double roomAVolume;
    private double roomBVolume;

    public ComputationModule() {
        totalTime = 360; // seconds
        steps = 360;
        roomATemperatures = new double[steps + 1];
        roomBTemperatures = new double[steps + 1];
        roomATemperatures[0] = 35.0; // initial temperature in Celsius
        roomBTemperatures[0] = 16.0; // initial temperature in Celsius
        Q = 1.7; // volumetric flow rate in cubic meters per second
        roomAVolume = 575.0; // cubic meters
        roomBVolume = 520.0; // cubic meters

        compute();
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public double[] getRoomATemperatures() {
        return roomATemperatures;
    }

    public void setRoomATemperatures(double[] roomATemperatures) {
        this.roomATemperatures = roomATemperatures;
    }

    public double[] getRoomBTemperatures() {
        return roomBTemperatures;
    }

    public void setRoomBTemperatures(double[] roomBTemperatures) {
        this.roomBTemperatures = roomBTemperatures;
    }

    public double getQ() {
        return Q;
    }

    public void setQ(double q) {
        Q = q;
    }

    public double getRoomAVolume() {
        return roomAVolume;
    }

    public void setRoomAVolume(double roomAVolume) {
        this.roomAVolume = roomAVolume;
    }

    public double getRoomBVolume() {
        return roomBVolume;
    }

    public void setRoomBVolume(double roomBVolume) {
        this.roomBVolume = roomBVolume;
    }

    private void compute() {
        double dt = totalTime / steps;
        for (int i = 0; i < steps; i++) {
            double dTbdt = (Q / roomBVolume) * (roomATemperatures[i] - roomBTemperatures[i]);
            double dTadt = (Q / roomAVolume) * (roomBTemperatures[i] - roomATemperatures[i]);

            roomBTemperatures[i + 1] = roomBTemperatures[i] + dTbdt * dt;
            roomATemperatures[i + 1] = roomATemperatures[i] + dTadt * dt;
        }
        print();
    }

    private void print() {
        double timePoint = 0.0;
        int stepIndex = 0;
        double dt = totalTime / steps;

        while (timePoint <= totalTime) {
            System.out.println("Temperature in room A at time: " + timePoint + " is: " + roomATemperatures[stepIndex]);
            System.out.println("Temperature in room B at time: " + timePoint + " is: " + roomBTemperatures[stepIndex]);
            System.out.println();

            if (stepIndex < steps) {
                stepIndex++;
            }

            for (double i = 0.0; i < dt; i++) {
                timePoint++;
            }
        }
    }
}
