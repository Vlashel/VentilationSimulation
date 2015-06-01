package com.vlashel.vent;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

import static com.vlashel.vent.Helper.pack;

public class DataModule {

    private double speed;
    private double serverRoomTemperature;
    private double officeRoomTemperature;
    private double volumetricFlowRate;
    private double serverRoomVolume;
    private double officeRoomVolume;
    private double serverRoomTemperatureMax;
    private double serverRoomTemperatureMin;
    private double outsideAirTemperature;
    private double serverRoomTemperatureIncrease;
    private double officeRoomTemperatureDecrease;
    private double precision;
    private int dt;

    private BooleanProperty recuperate = new SimpleBooleanProperty();
    private DoubleProperty desiredTemperature = new SimpleDoubleProperty();


    public DataModule() {
        speed = 0.1;
        serverRoomTemperature = 27.0; // initial temperature in Celsius
        officeRoomTemperature = 18.0; // initial temperature in Celsius
        volumetricFlowRate = 0.15; // volumetric flow rate in cubic meters per second
        serverRoomVolume = 30.0; // cubic meters 3 x 4 x 2.5
        officeRoomVolume = 140.0; // cubic meters 8 x 7 x 2.5
        serverRoomTemperatureMax = 27.0;
        serverRoomTemperatureMin = 22.0;
        outsideAirTemperature = 14.0;
        serverRoomTemperatureIncrease = 0.02598786;
        officeRoomTemperatureDecrease = 0.001;
        precision = 0.2;
        dt = 1;
    }

    public double getServerRoomTemperature() {
        return serverRoomTemperature;
    }

    public double getOfficeRoomTemperature() {
        return officeRoomTemperature;
    }

    public double getMaximumAchievableTemperature() {
        double officeRoomTemperature = this.officeRoomTemperature;
        double serverRoomTemperature = this.serverRoomTemperature;

        while (pack(serverRoomTemperature) <= serverRoomTemperatureMax) {
            double dTofficedt = ((volumetricFlowRate / officeRoomVolume) * (serverRoomTemperature - officeRoomTemperature)) - officeRoomTemperatureDecrease;
            double dTserverdt = ((volumetricFlowRate / serverRoomVolume) * (officeRoomTemperature - serverRoomTemperature)) + serverRoomTemperatureIncrease;

            officeRoomTemperature += dTofficedt * dt;
            serverRoomTemperature += dTserverdt * dt;
        }
        return officeRoomTemperature - precision;
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

    public int simulateAndGetTimeLeft() {
        int timePoint = 0;

        double officeRoomTemperature = this.officeRoomTemperature;
        double serverRoomTemperature = this.serverRoomTemperature;

        if (recuperate.get()) {
            while (pack(officeRoomTemperature) < pack(desiredTemperature.get()) && shouldRecuperateNow(officeRoomTemperature)) {
                if (shouldRecuperateNow(officeRoomTemperature) && pack(serverRoomTemperature) >= serverRoomTemperatureMax) {
                    while (pack(officeRoomTemperature) <= pack(desiredTemperature.get())
                            && pack(serverRoomTemperature) <= serverRoomTemperatureMax
                            && pack(serverRoomTemperature) >= serverRoomTemperatureMin
                            && recuperate.get()) {

                        double dTofficedt = ((volumetricFlowRate / officeRoomVolume) * (serverRoomTemperature - officeRoomTemperature)) - officeRoomTemperatureDecrease;
                        double dTserverdt = ((volumetricFlowRate / serverRoomVolume) * (officeRoomTemperature - serverRoomTemperature)) + serverRoomTemperatureIncrease;

                        officeRoomTemperature += dTofficedt * dt;
                        serverRoomTemperature += dTserverdt * dt;
                        timePoint += dt;
                    }
                } else {
                    if (pack(serverRoomTemperature) >= serverRoomTemperatureMax) {
                        while (pack(serverRoomTemperature) >= serverRoomTemperatureMin && !shouldRecuperateNow(officeRoomTemperature)) {

                            double dTserverdt = ((volumetricFlowRate / serverRoomVolume) * (outsideAirTemperature - serverRoomTemperature)) + serverRoomTemperatureIncrease;

                            serverRoomTemperature += dTserverdt * dt;

                            if (pack(officeRoomTemperature) > outsideAirTemperature) {
                                officeRoomTemperature -= officeRoomTemperatureDecrease * dt;
                            }
                            timePoint += dt;
                        }
                    } else {
                        serverRoomTemperature += serverRoomTemperatureIncrease * dt;
                        if (pack(officeRoomTemperature) > outsideAirTemperature) {
                            officeRoomTemperature -= officeRoomTemperatureDecrease * dt;
                        }
                        timePoint += dt;
                    }
                }
            }
        }
        return timePoint;
    }

    private boolean shouldRecuperateNow(double officeRoomTemperature) {
        return recuperate.get() && pack(desiredTemperature.get()) - pack(officeRoomTemperature) >= precision;
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

    public double getServerRoomTemperatureMax() {
        return serverRoomTemperatureMax;
    }

    public void setServerRoomTemperatureMax(double serverRoomTemperatureMax) {
        this.serverRoomTemperatureMax = serverRoomTemperatureMax;
    }

    public double getServerRoomTemperatureMin() {
        return serverRoomTemperatureMin;
    }

    public void setServerRoomTemperatureMin(double serverRoomTemperatureMin) {
        this.serverRoomTemperatureMin = serverRoomTemperatureMin;
    }

    public double getOutsideAirTemperature() {
        return outsideAirTemperature;
    }

    public void setOutsideAirTemperature(double outsideAirTemperature) {
        this.outsideAirTemperature = outsideAirTemperature;
    }

    public double getServerRoomTemperatureIncrease() {
        return serverRoomTemperatureIncrease;
    }

    public void setServerRoomTemperatureIncrease(double serverRoomTemperatureIncrease) {
        this.serverRoomTemperatureIncrease = serverRoomTemperatureIncrease;
    }

    public double getOfficeRoomTemperatureDecrease() {
        return officeRoomTemperatureDecrease;
    }

    public void setOfficeRoomTemperatureDecrease(double officeRoomTemperatureDecrease) {
        this.officeRoomTemperatureDecrease = officeRoomTemperatureDecrease;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public boolean getRecuperate() {
        return recuperate.get();
    }

    public BooleanProperty recuperateProperty() {
        return recuperate;
    }

    public void setRecuperate(boolean recuperate) {
        this.recuperate.set(recuperate);
    }

    public double getDesiredTemperature() {
        return desiredTemperature.get();
    }

    public DoubleProperty desiredTemperatureProperty() {
        return desiredTemperature;
    }

    public void setDesiredTemperature(double desiredTemperature) {
        this.desiredTemperature.set(desiredTemperature);
    }
}
