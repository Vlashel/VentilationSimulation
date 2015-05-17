package com.vlashel.vent;

import java.util.*;

public class DataModule implements Refreshable {
    private int totalTime;
    private int steps;
    private double speed;
    private List<Double> roomATemperatures;
    private List<Double> roomBTemperatures;
    private double volumetricFlowRate;
    private double roomAVolume;
    private double roomBVolume;

    public DataModule() {
        totalTime = 500; // seconds
        steps = 500;
        speed = 0.1;
        roomATemperatures = new ArrayList<>();
        roomBTemperatures = new ArrayList<>();
        roomATemperatures.add(24.0); // initial temperature in Celsius
        roomBTemperatures.add(16.0); // initial temperature in Celsius
        volumetricFlowRate = 0.2; // volumetric flow rate in cubic meters per second
        roomAVolume = 250.0; // cubic meters
        roomBVolume = 50.0; // cubic meters

        compute();
    }

    public int getTotalTime() {
        return totalTime;
    }

    public int getSteps() {
        return steps;
    }

    public List<Double> getRoomATemperatures() {
        return roomATemperatures;
    }

    public List<Double> getRoomBTemperatures() {
        return roomBTemperatures;
    }

    public double getMaximumAchievableTemperature() {
        return getInitialColderRoomTemperatures().get(getInitialColderRoomTemperatures().size() - 1);
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
        return Math.max(roomATemperatures.get(0), roomBTemperatures.get(0));
    }

    public double getLowestTemperature() {
        return Math.min(roomATemperatures.get(0), roomBTemperatures.get(0));
    }

    private void compute() {
        int roomAend = roomATemperatures.size() - 1;
        int roomBend = roomBTemperatures.size() - 1;

        double dt = 1;
        while (!Helper.cutPrecision(roomATemperatures.get(roomAend)).equals(Helper.cutPrecision(roomBTemperatures.get(roomBend)))) {
            double dTbdt = (volumetricFlowRate / roomBVolume) * (roomATemperatures.get(roomAend) - roomBTemperatures.get(roomBend));
            double dTadt = (volumetricFlowRate / roomAVolume) * (roomBTemperatures.get(roomAend) - roomATemperatures.get(roomBend));

            roomBTemperatures.add(roomBTemperatures.get(roomBend) + dTbdt * dt);
            roomATemperatures.add(roomATemperatures.get(roomAend) + dTadt * dt);

            roomAend = roomATemperatures.size() - 1;
            roomBend = roomATemperatures.size() - 1;
        }
        totalTime = roomATemperatures.size();
        print();
    }


    public void setRoomAInitialTemperature(double roomATemperature) {
        this.roomATemperatures.clear();
        this.roomATemperatures.add(roomATemperature);
    }

    public void setRoomBInitialTemperature(double roomBTemperature) {
        this.roomBTemperatures.clear();
        this.roomBTemperatures.add(roomBTemperature);
    }

    public double getRoomAInitialTemperature() {
        return this.roomATemperatures.get(0);
    }

    public double getRoomBInitialTemperature() {
        return this.roomBTemperatures.get(0);
    }

    public List<Double> getInitialHotterRoomTemperatures() {
        return roomATemperatures.get(0) > roomBTemperatures.get(0) ? roomATemperatures : roomBTemperatures;
    }

    public List<Double> getInitialColderRoomTemperatures() {
        return roomATemperatures.get(0) < roomBTemperatures.get(0) ? roomATemperatures : roomBTemperatures;
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
        int timePoint = 0;
        while (timePoint < roomBTemperatures.size() && timePoint < roomATemperatures.size()) {
            System.out.println("Temperature in room A at time: " + timePoint + " is: " + roomATemperatures.get(timePoint));
            System.out.println("Temperature in room B at time: " + timePoint + " is: " + roomBTemperatures.get(timePoint));
            timePoint++;
            System.out.println();
        }
    }
}
