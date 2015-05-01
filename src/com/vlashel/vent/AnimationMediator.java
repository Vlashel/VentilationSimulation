package com.vlashel.vent;

import javafx.animation.KeyFrame;
import javafx.event.ActionEvent;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnimationMediator {
    private StartButton startButton;
    private StopButton stopButton;
    private TemperatureSlider temperatureSlider;
    private DataModule dataModule;
    private TemperaturesChart temperaturesChart;
    private TemperatureIndicator roomATemperature;
    private TemperatureIndicator roomBTemperature;
    private List<Ventilator> ventilators;
    private List<Refreshable> refreshables = new ArrayList<>();
    private List<Animatable> animatables = new ArrayList<>();

    public void registerVentilators(Ventilator... ventilators) {
        this.ventilators = Arrays.asList(ventilators);
        registerAnimatables(ventilators);
    }

    public void registerTemperatureSlider(TemperatureSlider temperatureSlider) {
        this.temperatureSlider = temperatureSlider;
        registerRefreshables(temperatureSlider);
    }

    public void registerAnimatables(Animatable... animatables) {
        Arrays.asList(animatables).forEach(this.animatables::add);
    }

    public void registerDataModule(DataModule dataModule) {
        this.dataModule = dataModule;
        registerRefreshables(dataModule);
    }

    public void registerStartButton(StartButton startButton) {
        this.startButton = startButton;
    }

    public void registerStopButton(StopButton stopButton) {
        this.stopButton = stopButton;
    }

    public void registerTemperaturesChart(TemperaturesChart temperaturesChart) {
        this.temperaturesChart = temperaturesChart;
        registerRefreshables(temperaturesChart);
        registerAnimatables(temperaturesChart);
    }

    public void registerRoomATemperatureIndicator(TemperatureIndicator roomATemperature) {
        this.roomATemperature = roomATemperature;
        setRoomATemperatureIndicatorValue(dataModule.getRoomATemperatures()[0]);
    }

    public void registerRoomBTemperatureIndicator(TemperatureIndicator roomBTemperature) {
        this.roomBTemperature = roomBTemperature;
        setRoomBTemperatureIndicatorValue(dataModule.getRoomBTemperatures()[0]);
    }

    public void registerRefreshables(Refreshable... refreshables) {
        Arrays.asList(refreshables).forEach(this.refreshables::add);
    }

    public void refresh() {
        this.refreshables.forEach(Refreshable::refresh);
    }

    public void startAnimation() {
        refresh();
        prepareAnimation();
        animatables.forEach(Animatable::play);
    }

    public void finishAnimation() {
       animatables.forEach(Animatable::stop);
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
                        updateInitialTemperatures(roomATemperature, roomBTemperature);
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

    private void updateInitialTemperatures(double roomATemperature, double roomBTemperature) {
        dataModule.setInitialTemperatures(roomATemperature, roomBTemperature);
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

    public void enableStopButton() {
        stopButton.enable();
    }

    public void enableStartButton() {
        startButton.enable();
    }

    public void disableStopButton() {
        stopButton.disable();
    }

    public void disableStartButton() {
        startButton.disable();
    }

    public void setRoomATemperatureIndicatorValue(double temperature) {
        roomATemperature.setText(String.format("%.1f", temperature));
    }

    public void setRoomBTemperatureIndicatorValue(double temperature) {
        roomBTemperature.setText(String.format("%.1f", temperature));
    }

    public void refreshTemperatureSlider() {
        temperatureSlider.refresh();
    }

    public void enableTemperatureSlider() {
        temperatureSlider.enable();
    }

    public void disableTemperatureSlider() {
        temperatureSlider.disable();
    }
}
