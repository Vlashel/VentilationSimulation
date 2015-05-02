package com.vlashel.vent;

public class DataModule implements Refreshable {
    private int totalTime;
    private int steps;
    private double[] roomATemperatures;
    private double[] roomBTemperatures;
    private double Q;
    private double roomAVolume;
    private double roomBVolume;

    public DataModule() {
        totalTime = 270; // seconds
        steps = 270;
        roomATemperatures = new double[steps + 1];
        roomBTemperatures = new double[steps + 1];
        roomATemperatures[0] = 35.0; // initial temperature in Celsius
        roomBTemperatures[0] = 11.0; // initial temperature in Celsius
        Q = 4.5; // volumetric flow rate in cubic meters per second
        roomAVolume = 575.0; // cubic meters
        roomBVolume = 520.0; // cubic meters

        compute();
    }

    public int getTotalTime() {
        return totalTime;
    }

    public int getSteps() {
        return steps;
    }

    public double[] getRoomATemperatures() {
        return roomATemperatures;
    }

    public double[] getRoomBTemperatures() {
        return roomBTemperatures;
    }

    public double getMaximumAchievableTemperature() {
        return roomATemperatures[0] < roomBTemperatures[0] ?
                roomATemperatures[steps] : roomBTemperatures[steps];
    }

    public double getHighestTemperature() {
        return Math.max(roomATemperatures[0], roomBTemperatures[0]);
    }

    public double getLowestTemperature() {
        return Math.min(roomATemperatures[0], roomBTemperatures[0]);
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

    public void setInitialTemperatures(double roomATemperature, double roomBTemperature) {
        this.roomATemperatures[0] = roomATemperature;
        this.roomBTemperatures[0] = roomBTemperature;
    }

    public double[] getInitialHotterRoomTemperatures() {
        return roomATemperatures[0] > roomBTemperatures[0] ? roomATemperatures : roomBTemperatures;
    }

    public double[] getInitialColderRoomTemperatures() {
        return roomATemperatures[0] < roomBTemperatures[0] ? roomATemperatures : roomBTemperatures;
    }

    public double getVolumetricFlowRate() {
        return Q;
    }

    public double getRoomAVolume() {
        return roomAVolume;
    }

    public double getRoomBVolume() {
        return roomBVolume;
    }

    @Override
    public void refresh() {
        compute();
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
