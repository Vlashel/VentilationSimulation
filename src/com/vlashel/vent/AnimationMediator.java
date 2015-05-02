package com.vlashel.vent;

import javafx.animation.KeyFrame;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AnimationMediator {
    private StartButton startButton;
    private StopButton stopButton;
    private TemperatureSlider temperatureSlider;
    private DataModule dataModule;
    private TemperaturesChart temperaturesChart;
    private CurrentTemperatureIndicator roomATemperature;
    private CurrentTemperatureIndicator roomBTemperature;
    private DesiredTemperatureIndicator desiredTemperatureIndicator;
    private ElapsedTimeIndicator elapsedTimeIndicator;
    private List<Ventilator> ventilators;
    private ElapsedTimeCounter elapsedTimeCounter = new ElapsedTimeCounter();
    private List<Refreshable> refreshables = new ArrayList<>();
    private List<Animatable> animatables = new ArrayList<>();

    private DoubleProperty desiredTemperature = new SimpleDoubleProperty();

    public void registerVentilators(Ventilator... ventilators) {
        this.ventilators = Arrays.asList(ventilators);
        registerAnimatables(ventilators);
    }

    public void registerTemperatureSlider(TemperatureSlider temperatureSlider) {
        this.temperatureSlider = temperatureSlider;
        desiredTemperature.bind(temperatureSlider.valueProperty());
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

    public void registerRoomATemperatureIndicator(CurrentTemperatureIndicator roomATemperature) {
        this.roomATemperature = roomATemperature;
        setRoomATemperatureIndicatorValue(dataModule.getRoomATemperatures()[0]);
    }

    public void registerRoomBTemperatureIndicator(CurrentTemperatureIndicator roomBTemperature) {
        this.roomBTemperature = roomBTemperature;
        setRoomBTemperatureIndicatorValue(dataModule.getRoomBTemperatures()[0]);
    }

    public void registerElapsedTimeIndicator(ElapsedTimeIndicator elapsedTimeIndicator) {
        this.elapsedTimeIndicator = elapsedTimeIndicator;
    }

    public void registerRefreshables(Refreshable... refreshables) {
        Arrays.asList(refreshables).forEach(this.refreshables::add);
    }

    public void registerDesiredTemperatureIndicator(DesiredTemperatureIndicator desiredTemperatureIndicator) {
        this.desiredTemperatureIndicator = desiredTemperatureIndicator;
        setDesiredTemperatureIndicatorValue(desiredTemperature.get());
        desiredTemperature.addListener((o, oldValue, newValue) ->
                setDesiredTemperatureIndicatorValue(newValue.doubleValue()));

    }

    public void refresh() {
        this.refreshables.forEach(Refreshable::refresh);
    }

    public void startAnimation() {
        refresh();
        prepareAnimation();
        enableStopButton();
        disableTemperatureSlider();
        disableStartButton();
        animatables.forEach(Animatable::play);
    }

    public void finishAnimation() {
        animatables.forEach(Animatable::stop);
        refreshTemperatureSlider();
        enableTemperatureSlider();
        enableStartButton();
        disableStopButton();
    }

    private void prepareAnimation() {
        double speed = 1.0;

        int stepIndex = 1;
        int timePoint = 1;
        double dt = dataModule.getTotalTime() / dataModule.getSteps();

        while (timePoint <= dataModule.getTotalTime()
                && cutPrecision(dataModule.getInitialColderRoomTemperatures()[stepIndex]) <= cutPrecision(desiredTemperature.get())) {

            double roomATemperature = dataModule.getRoomATemperatures()[stepIndex];
            double roomBTemperature = dataModule.getRoomBTemperatures()[stepIndex];
            int timePointCopy = timePoint;

            temperaturesChart.getAnimation().getKeyFrames().add(
                    new KeyFrame(Duration.millis(timePoint * 1000 * speed), (ActionEvent e) -> {
                        temperatureAchievedCallback(timePointCopy, roomATemperature, roomBTemperature);
                        updateInitialTemperatures(roomATemperature, roomBTemperature);
                        incrementElapsedTimeCounter();
                        updateElapsedTimeIndicatorValue();
                    })
            );

            if (stepIndex < dataModule.getSteps()) {
                stepIndex++;
            }
            for (int i = 0; i < dt; i++) {
                timePoint++;
            }
        }

        temperaturesChart.getAnimation().setOnFinished((ActionEvent event) -> finishAnimation());
    }

    private void updateInitialTemperatures(double roomATemperature, double roomBTemperature) {
        dataModule.setInitialTemperatures(roomATemperature, roomBTemperature);
    }

    private void updateElapsedTimeIndicatorValue() {
        elapsedTimeIndicator.setText(String.valueOf(elapsedTimeCounter.getElapsedTime()));
    }

    private void incrementElapsedTimeCounter() {
        elapsedTimeCounter.increment();
    }

    private void temperatureAchievedCallback(int timePoint,
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

    public void setDesiredTemperatureIndicatorValue(double temperature) {
        desiredTemperatureIndicator.setText(String.format("%.1f", temperature));
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

    private double cutPrecision(double value) {
        return Double.valueOf(String.format(Locale.ENGLISH, "%.1f", value));
    }
}
