package com.vlashel.vent;

import javafx.animation.Animation;
import javafx.event.ActionEvent;

import java.util.List;


public class Mediator {
    private Animation animation;
    private ToggleSwitch toggleSwitch;
    private TemperaturesChart temperaturesChart;
    private TemperatureIndicator roomATemperature;
    private TemperatureIndicator roomBTemperature;
    private List<Ventilator> ventilators;

    public void registerVentilators(List<Ventilator> ventilators) {
        this.ventilators = ventilators;
    }

    public void registerToggleSwitch(ToggleSwitch toggleSwitch) {
        this.toggleSwitch = toggleSwitch;
    }

    public void registerTemperaturesChart(TemperaturesChart temperaturesChart) {
        this.temperaturesChart = temperaturesChart;
    }

    public void registerRoomATemperature(TemperatureIndicator roomATemperature) {
        this.roomATemperature = roomATemperature;
    }

    public void registerRoomBTemperature(TemperatureIndicator roomBTemperature) {
        this.roomBTemperature = roomBTemperature;
    }

    public void registerAnimation(Animation animation) {
        this.animation = animation;
    }

    public void setRoomATemperatureIndicatorValue(double temperature) {
        roomATemperature.setText(String.format("%.1f", temperature));
    }

    public void setRoomBTemperatureIndicatorValue(double temperature) {
        roomBTemperature.setText(String.format("%.1f", temperature));
    }

    public void animate() {
        animation.play();
    }

    public void finish() {
        animation.stop();
        ventilators.forEach(Ventilator::stopVentilatorGradually);
    }
}
