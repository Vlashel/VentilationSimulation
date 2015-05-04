package com.vlashel.vent;

public class DataModule implements Refreshable {
    private int totalTime;
    private int steps;
    private int limit;
    private double speed;
    private double[] roomATemperatures;
    private double[] roomBTemperatures;
    private double volumetricFlowRate;
    private double roomAVolume;
    private double roomBVolume;

    public DataModule() {
        totalTime = 1000; // seconds
        steps = 1000;
        limit = 0;
        speed = 1.0;
        roomATemperatures = new double[steps + 1];
        roomBTemperatures = new double[steps + 1];
        roomATemperatures[0] = 35.0; // initial temperature in Celsius
        roomBTemperatures[0] = 11.0; // initial temperature in Celsius
        volumetricFlowRate = 6.0; // volumetric flow rate in cubic meters per second
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
        return getInitialColderRoomTemperatures()[limit];
    }

    public void setVolumetricFlowRate(double volumetricFlowRate) {
        this.volumetricFlowRate = volumetricFlowRate;
    }

    public void setRoomAVolume(double roomAVolume) {
        this.roomAVolume = roomAVolume;
    }

    public void setRoomBVolume(double roomBVolume) {
        this.roomBVolume = roomBVolume;
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
            double dTbdt = (volumetricFlowRate / roomBVolume) * (roomATemperatures[i] - roomBTemperatures[i]);
            double dTadt = (volumetricFlowRate / roomAVolume) * (roomBTemperatures[i] - roomATemperatures[i]);

            roomBTemperatures[i + 1] = roomBTemperatures[i] + dTbdt * dt;
            roomATemperatures[i + 1] = roomATemperatures[i] + dTadt * dt;
        }
        findLimit();
        print();
    }

    private void findLimit() {
        double[] colderRoomTemperatures = getInitialColderRoomTemperatures();

        for (int i = 0; i < colderRoomTemperatures.length - 1; i++) {
            double temp = Helper.cutPrecision(colderRoomTemperatures[i], "%.2f");
            double nextTemp = Helper.cutPrecision(colderRoomTemperatures[i + 1], "%.2f");

            if (temp == nextTemp) {
                limit = i;
                System.out.println("Limit is: " + limit);
                break;
            }
        }
    }

    public void setRoomAInitialTemperature(double roomATemperature) {
        this.roomATemperatures[0] = roomATemperature;
    }

    public void setRoomBInitialTemperature(double roomBTemperature) {
        this.roomBTemperatures[0] = roomBTemperature;
    }

    public double getRoomAInitialTemperature() {
        return this.roomATemperatures[0];
    }

    public double getRoomBInitialTemperature() {
        return this.roomBTemperatures[0];
    }

    public double[] getInitialHotterRoomTemperatures() {
        return roomATemperatures[0] > roomBTemperatures[0] ? roomATemperatures : roomBTemperatures;
    }

    public double[] getInitialColderRoomTemperatures() {
        return roomATemperatures[0] < roomBTemperatures[0] ? roomATemperatures : roomBTemperatures;
    }

    public double getVolumetricFlowRate() {
        return volumetricFlowRate;
    }

    public double getRoomAVolume() {
        return roomAVolume;
    }

    public double getRoomBVolume() {
        return roomBVolume;
    }

    public int getLimit() {
        return limit;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
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
