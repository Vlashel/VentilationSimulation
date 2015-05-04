package com.vlashel.vent;

import com.vlashel.vent.button.SettingsButton;
import com.vlashel.vent.button.StartButton;
import com.vlashel.vent.button.StopButton;
import com.vlashel.vent.indicator.*;
import com.vlashel.vent.input.TemperatureSliderInput;
import com.vlashel.vent.window.AlertBoxWindow;
import com.vlashel.vent.window.SettingsWindow;
import javafx.animation.KeyFrame;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static com.vlashel.vent.Helper.*;

public class AnimationMediator {
    private StartButton startButton;
    private StopButton stopButton;
    private SettingsButton settingsButton;
    private TemperatureSliderInput temperatureSliderInput;
    private DataModule dataModule;
    private TemperaturesChartIndicator temperaturesChartIndicator;
    private RoomATemperatureIndicator roomATemperature;
    private RoomBTemperatureIndicator roomBTemperature;
    private DesiredTemperatureIndicator desiredTemperatureIndicator;
    private RoomAVolumeIndicator roomAVolumeIndicator;
    private RoomBVolumeIndicator roomBVolumeIndicator;
    private VolumetricFlowRateIndicator volumetricFlowRateIndicator;
    private ElapsedTimeIndicator elapsedTimeIndicator;
    private SettingsWindow settingsWindow;
    private List<Ventilator> ventilators;
    private ElapsedTimeCounter elapsedTimeCounter = new ElapsedTimeCounter();
    private List<Refreshable> refreshables = new ArrayList<>();
    private List<Animatable> animatables = new ArrayList<>();

    private DoubleProperty desiredTemperature = new SimpleDoubleProperty();

    public void registerVentilators(Ventilator... ventilators) {
        this.ventilators = Arrays.asList(ventilators);
        registerAnimatables(ventilators);
    }

    public void registerTemperatureSlider(TemperatureSliderInput temperatureSliderInput) {
        this.temperatureSliderInput = temperatureSliderInput;
        desiredTemperature.bind(temperatureSliderInput.valueProperty());
        registerRefreshables(temperatureSliderInput);
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

    public void registerSettingsButton(SettingsButton settingsButton) {
        this.settingsButton = settingsButton;
    }

    public void registerTemperaturesChart(TemperaturesChartIndicator temperaturesChartIndicator) {
        this.temperaturesChartIndicator = temperaturesChartIndicator;
        registerRefreshables(temperaturesChartIndicator);
        registerAnimatables(temperaturesChartIndicator);
    }

    public void registerRoomATemperatureIndicator(RoomATemperatureIndicator roomATemperature) {
        this.roomATemperature = roomATemperature;
        registerRefreshables(roomATemperature);
    }

    public void registerRoomBTemperatureIndicator(RoomBTemperatureIndicator roomBTemperature) {
        this.roomBTemperature = roomBTemperature;
        registerRefreshables(roomBTemperature);
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

    public void registerInitialConditionsWindow(SettingsWindow settingsWindow) {
        this.settingsWindow = settingsWindow;
    }

    public void registerRoomAVolumeIndicator(RoomAVolumeIndicator roomAVolumeIndicator) {
        this.roomAVolumeIndicator = roomAVolumeIndicator;
        registerRefreshables(roomAVolumeIndicator);
    }

    public void registerRoomBVolumeIndicator(RoomBVolumeIndicator roomBVolumeIndicator) {
        this.roomBVolumeIndicator = roomBVolumeIndicator;
        registerRefreshables(roomBVolumeIndicator);
    }

    public void registerVolumetricFlowRateIndicator(VolumetricFlowRateIndicator volumetricFlowRateIndicator) {
        this.volumetricFlowRateIndicator = volumetricFlowRateIndicator;
        registerRefreshables(volumetricFlowRateIndicator);
    }

    public void refresh() {
        this.refreshables.forEach(Refreshable::refresh);
    }

    public void startAnimation() {
        refresh();
        prepareAnimation();
        enableStopButton();
        disableSettingsButton();
        disableTemperatureSlider();
        disableStartButton();
        animatables.forEach(Animatable::play);
    }

    public void finishAnimation() {
        animatables.forEach(Animatable::stop);
        refreshTemperatureSlider();
        enableTemperatureSlider();
        enableStartButton();
        enableSettingsButton();
        disableStopButton();
    }

    private void prepareAnimation() {
        double speed = dataModule.getSpeed();

        int stepIndex = 1;
        int timePoint = 1;
        double dt = dataModule.getTotalTime() / dataModule.getSteps();

        while (timePoint <= dataModule.getTotalTime()
                && cutPrecision(dataModule.getInitialColderRoomTemperatures()[stepIndex]) <= cutPrecision(desiredTemperature.get())) {

            double roomATemperature = dataModule.getRoomATemperatures()[stepIndex];
            double roomBTemperature = dataModule.getRoomBTemperatures()[stepIndex];
            int timePointCopy = timePoint;

            temperaturesChartIndicator.getAnimation().getKeyFrames().add(
                    new KeyFrame(Duration.millis(timePoint * 1000 * speed), (ActionEvent e) -> {
                        temperatureAchievedCallback(timePointCopy, roomATemperature, roomBTemperature);
                        setInitialTemperatures(roomATemperature, roomBTemperature);
                        incrementElapsedTimeCounter();
                        setElapsedTimeIndicatorValue();
                    })
            );

            if (cutPrecision(dataModule.getInitialColderRoomTemperatures()[stepIndex]) == cutPrecision(desiredTemperature.get())) {
                break;
            }

            if (stepIndex < dataModule.getSteps()) {
                stepIndex++;
            }
            for (int i = 0; i < dt; i++) {
                timePoint++;
            }
        }

        temperaturesChartIndicator.getAnimation().setOnFinished((ActionEvent event) -> finishAnimation());
    }

    private void setInitialTemperatures(double roomATemperature, double roomBTemperature) {
        dataModule.setRoomAInitialTemperature(roomATemperature);
        dataModule.setRoomBInitialTemperature(roomBTemperature);
    }

    private void setElapsedTimeIndicatorValue() {
        elapsedTimeIndicator.setText(String.valueOf(elapsedTimeCounter.getElapsedTime()));
    }

    private void incrementElapsedTimeCounter() {
        elapsedTimeCounter.increment();
    }

    private void temperatureAchievedCallback(int timePoint,
                                             double roomACurrentTemperature,
                                             double roomBCurrentTemperature) {
        temperaturesChartIndicator.plot(
                timePoint,
                roomACurrentTemperature,
                roomBCurrentTemperature
        );

        setRoomATemperatureIndicatorValue(roomACurrentTemperature);
        setRoomBTemperatureIndicatorValue(roomBCurrentTemperature);
    }

    public void enableSettingsButton() {
        settingsButton.enable();
    }

    public void disableSettingsButton() {
        settingsButton.disable();
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
        roomATemperature.setText(String.valueOf(cutPrecision(temperature)));
    }

    public void setRoomBTemperatureIndicatorValue(double temperature) {
        roomBTemperature.setText(String.valueOf(cutPrecision(temperature)));
    }

    public void setDesiredTemperatureIndicatorValue(double temperature) {
        desiredTemperatureIndicator.setText(String.valueOf(cutPrecision(temperature)));
    }

    public void refreshTemperatureSlider() {
        temperatureSliderInput.refresh();
    }

    public void enableTemperatureSlider() {
        temperatureSliderInput.enable();
    }

    public void disableTemperatureSlider() {
        temperatureSliderInput.disable();
    }

    public void displaySettingsWindow() {
        settingsWindow.display();
    }

    public void displayAlertBox(String title, String message) {
        new AlertBoxWindow().display(title, message);
    }

    public DoubleProperty desiredTemperatureProperty() {
        return desiredTemperature;
    }


}
