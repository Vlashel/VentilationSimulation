package com.vlashel.vent;

import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.event.ActionEvent;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class AnimationMediator {
    private ParallelTransition applicationAnimation;
    private ToggleSwitch toggleSwitch;
    private DataModule dataModule;
    private TemperaturesChart temperaturesChart;
    private TemperatureIndicator roomATemperature;
    private TemperatureIndicator roomBTemperature;
    private List<Ventilator> ventilators = new ArrayList<>();

    public void registerVentilators(Ventilator... ventilators) {
        for (Ventilator v : ventilators) {
            this.ventilators.add(v);
        }
    }

    public void registerDataModule(DataModule dataModule) {
        this.dataModule = dataModule;
    }

    public void registerToggleSwitch(ToggleSwitch toggleSwitch) {
        this.toggleSwitch = toggleSwitch;
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

    public void registerApplicationAnimation(ParallelTransition animation) {
        this.applicationAnimation = animation;
    }

    public void startAnimation() {
        prepareAnimation();
        applicationAnimation.play();
    }

    public void finishAnimation() {
        applicationAnimation.stop();
        ventilators.forEach(Ventilator::stopVentilatorGradually);
    }


    private void prepareVentilators() {
        ventilators.forEach(v -> applicationAnimation.getChildren().add(v.getAnimation()));
    }

    private void prepareAnimation() {
        prepareVentilators();

        double speed = 0.05;
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
