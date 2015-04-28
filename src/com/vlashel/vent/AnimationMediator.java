package com.vlashel.vent;

import javafx.animation.KeyFrame;
import javafx.event.ActionEvent;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;

public class AnimationMediator {
    private StartButton startButton;
    private DataModule dataModule;
    private TemperaturesChart temperaturesChart;
    private TemperatureIndicator roomATemperature;
    private TemperatureIndicator roomBTemperature;
    private List<Ventilator> ventilators;

    public void registerVentilators(Ventilator... ventilators) {
        this.ventilators = Arrays.asList(ventilators);
    }

    public void registerDataModule(DataModule dataModule) {
        this.dataModule = dataModule;
    }

    public void registerToggleSwitch(StartButton startButton) {
        this.startButton = startButton;
    }

    public void registerTemperaturesChart(TemperaturesChart temperaturesChart) {
        this.temperaturesChart = temperaturesChart;
    }

    public void registerRoomATemperatureIndicator(TemperatureIndicator roomATemperature) {
        this.roomATemperature = roomATemperature;
    }

    public void registerRoomBTemperatureIndicator(TemperatureIndicator roomBTemperature) {
        this.roomBTemperature = roomBTemperature;
    }

    public void startAnimation() {
        prepareAnimation();
        temperaturesChart.play();
        ventilators.forEach(Ventilator::play);
    }

    public void finishAnimation() {
        ventilators.forEach(Ventilator::stopVentilatorGradually);
        temperaturesChart.stop();
    }

    private void prepareAnimation() {
        double speed = 0.1;

        int stepIndex = 0;
        double timePoint = 0.0;
        double dt = dataModule.getTotalTime() / dataModule.getSteps();

        while (timePoint <= dataModule.getTotalTime()) {
            double roomATemperature = dataModule.getRoomATemperatures()[stepIndex];
            double roomBTemperature = dataModule.getRoomBTemperatures()[stepIndex];
            double timePointCopy = timePoint;

            temperaturesChart.getAnimation().getKeyFrames().add(
                    new KeyFrame(Duration.millis(timePoint * 1000 * speed), (ActionEvent e) -> {
                        temperatureAchievedCallback(timePointCopy, roomATemperature, roomBTemperature);
                    })
            );

            if (stepIndex < dataModule.getSteps()) {
                stepIndex++;
            }
            for (double i = 0; i < dt; i++) {
                timePoint++;
            }
        }

        temperaturesChart.getAnimation().setOnFinished((ActionEvent event) -> finishAnimation());
    }

    private void temperatureAchievedCallback(double timePoint,
                                             double roomACurrentTemperature,
                                             double roomBCurrentTemperature) {
        temperaturesChart.plot(
                timePoint,
                roomACurrentTemperature,
                roomBCurrentTemperature
        );

        setRoomATemperatureIndicatorValue(roomACurrentTemperature);
        setRoomBTemperatureIndicatorValue(roomBCurrentTemperature);
    }

    public void setRoomATemperatureIndicatorValue(double temperature) {
        roomATemperature.setText(String.format("%.1f", temperature));
    }

    public void setRoomBTemperatureIndicatorValue(double temperature) {
        roomBTemperature.setText(String.format("%.1f", temperature));
    }
}
