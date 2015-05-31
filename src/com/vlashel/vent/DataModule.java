package com.vlashel.vent;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

import static com.vlashel.vent.Helper.pack;

public class DataModule implements Refreshable {
    private double speed;
    private Double serverRoomTemperature;
    private Double officeRoomTemperature;
    private double volumetricFlowRate;
    private double serverRoomVolume;
    private double officeRoomVolume;
    private double serverRoomTempMax;
    private double serverRoomTempMin;
    private double outsideAirTemp;
    private double serverRoomTempIncrease;
    private double officeRoomTempDecrease;

    private BooleanProperty recuperateToOfficeRoom = new SimpleBooleanProperty();
    private DoubleProperty desiredTemperature = new SimpleDoubleProperty();
    private ControllerMediator controllerMediator;


    public DataModule(ControllerMediator controllerMediator) {
        this.controllerMediator = controllerMediator;
        speed = 0.01;
        serverRoomTemperature = 27.0; // initial temperature in Celsius
        officeRoomTemperature = 18.0; // initial temperature in Celsius
        volumetricFlowRate = 0.15; // volumetric flow rate in cubic meters per second
        serverRoomVolume = 30.0; // cubic meters 3 x 4 x 2.5
        officeRoomVolume = 140.0; // cubic meters 8 x 7 x 2.5
        serverRoomTempMax = 27.0;
        serverRoomTempMin = 22.0;
        outsideAirTemp = 14.0;
        serverRoomTempIncrease = 0.02598786;
        officeRoomTempDecrease = 0.001;

        recuperateToOfficeRoom.bind(controllerMediator.isRecuperationOnProperty());
        desiredTemperature.bind(controllerMediator.desiredTemperatureProperty());
    }

    public Double getServerRoomTemperature() {
        return serverRoomTemperature;
    }

    public Double getOfficeRoomTemperature() {
        return officeRoomTemperature;
    }

    public double getMaximumAchievableTemperature() {
        double officeRoomTemperature = this.officeRoomTemperature;
        double serverRoomTemperature = this.serverRoomTemperature;

        int dt = 1;
        while (pack(serverRoomTemperature) <= serverRoomTempMax) {
            double dTofficedt = ((volumetricFlowRate / officeRoomVolume) * (serverRoomTemperature - officeRoomTemperature)) - officeRoomTempDecrease;
            double dTserverdt = ((volumetricFlowRate / serverRoomVolume) * (officeRoomTemperature - serverRoomTemperature)) + serverRoomTempIncrease;

            officeRoomTemperature += dTofficedt * dt;
            serverRoomTemperature += dTserverdt * dt;
        }
        return officeRoomTemperature - 0.2;
    }

    public void setVolumetricFlowRate(double volumetricFlowRate) {
        this.volumetricFlowRate = volumetricFlowRate;
    }

    public void setServerRoomVolume(double serverRoomVolume) {
        this.serverRoomVolume = serverRoomVolume;
    }

    public void setOfficeRoomVolume(double officeRoomVolume) {
        this.officeRoomVolume = officeRoomVolume;
    }

    public double getHighestTemperature() {
        return Math.max(serverRoomTemperature, officeRoomTemperature);
    }

    public double getLowestTemperature() {
        return officeRoomTemperature;
    }

    public int simulateAndGetTimeLeft() {
        int dt = 1;
        int timePoint = 0;

        double officeRoomTemperature = this.officeRoomTemperature;
        double serverRoomTemperature = this.serverRoomTemperature;

        if (recuperateToOfficeRoom.get()) {
            while (pack(officeRoomTemperature) < pack(desiredTemperature.get()) && checkWhetherShouldRecuperateNow()) {
                if (checkWhetherShouldRecuperateNow() && pack(serverRoomTemperature) >= serverRoomTempMax) {
                    while (pack(officeRoomTemperature) <= pack(desiredTemperature.get()) && pack(serverRoomTemperature) <= serverRoomTempMax) {

                        double dTofficedt = ((volumetricFlowRate / officeRoomVolume) * (serverRoomTemperature - officeRoomTemperature)) - officeRoomTempDecrease;
                        double dTserverdt = ((volumetricFlowRate / serverRoomVolume) * (officeRoomTemperature - serverRoomTemperature)) + serverRoomTempIncrease;

                        officeRoomTemperature += dTofficedt * dt;
                        serverRoomTemperature += dTserverdt * dt;
                        timePoint += dt;
                    }
                } else {
                    if (pack(serverRoomTemperature) >= serverRoomTempMax) {
                        while (pack(serverRoomTemperature) >= serverRoomTempMin && !checkWhetherShouldRecuperateNow()) {

                            double dTserverdt = ((volumetricFlowRate / serverRoomVolume) * (outsideAirTemp - serverRoomTemperature)) + serverRoomTempIncrease;

                            serverRoomTemperature += dTserverdt * dt;

                            if (pack(officeRoomTemperature) > outsideAirTemp) {
                                officeRoomTemperature -= officeRoomTempDecrease * dt;
                            }
                            timePoint += dt;
                        }
                    } else {
                        serverRoomTemperature += serverRoomTempIncrease * dt;
                        if (pack(officeRoomTemperature) > outsideAirTemp) {
                            officeRoomTemperature -= officeRoomTempDecrease * dt;
                        }
                        timePoint += dt;
                    }
                }
            }
        }
        return timePoint;
    }

    private boolean checkWhetherShouldRecuperateNow() {
        return recuperateToOfficeRoom.get() && pack(desiredTemperature.get()) - pack(officeRoomTemperature) >= 0.1;
    }


    public void setServerRoomTemperature(double serverRoomTemperature) {
        this.serverRoomTemperature = serverRoomTemperature;
    }

    public void setOfficeRoomTemperature(double officeRoomTemperature) {
        this.officeRoomTemperature = officeRoomTemperature;
    }

    public double getVolumetricFlowRate() {
        return volumetricFlowRate;
    }

    public double getServerRoomVolume() {
        return serverRoomVolume;
    }

    public double getOfficeRoomVolume() {
        return officeRoomVolume;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public void refresh() {
        simulateAndGetTimeLeft();
    }

    @Override
    public String toString() {
        return "DataModule{" +
                "speed=" + speed +
                ", serverRoomTemperature=" + serverRoomTemperature +
                ", officeRoomTemperature=" + officeRoomTemperature +
                ", volumetricFlowRate=" + volumetricFlowRate +
                ", serverRoomVolume=" + serverRoomVolume +
                ", officeRoomVolume=" + officeRoomVolume +
                ", serverRoomTempMax=" + serverRoomTempMax +
                ", serverRoomTempMin=" + serverRoomTempMin +
                ", outsideAirTemp=" + outsideAirTemp +
                ", serverRoomTempIncrease=" + serverRoomTempIncrease +
                ", officeRoomTempDecrease=" + officeRoomTempDecrease +
                ", recuperateToOfficeRoom=" + recuperateToOfficeRoom +
                ", desiredTemperature=" + desiredTemperature +
                ", controllerMediator=" + controllerMediator +
                '}';
    }
}
