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
    private ElapsedTimeIndicator elapsedTimeIndicator;
    private SettingsWindow settingsWindow;
    private ElapsedTimeCounter elapsedTimeCounter = new ElapsedTimeCounter();
    private List<Refreshable> refreshables = new ArrayList<>();
    private List<Animatable> animatables = new ArrayList<>();

    public AnimationMediator() {
        init();
    }

    private void init() {
        elapsedTimeCounter = new ElapsedTimeCounter();
        registerRefreshables(elapsedTimeCounter);
    }

    private DoubleProperty desiredTemperature = new SimpleDoubleProperty();

    public void registerVentilators(Ventilator... ventilators) {
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
        registerRefreshables(roomAVolumeIndicator);
    }

    public void registerRoomBVolumeIndicator(RoomBVolumeIndicator roomBVolumeIndicator) {
        registerRefreshables(roomBVolumeIndicator);
    }

    public void registerVolumetricFlowRateIndicator(VolumetricFlowRateIndicator volumetricFlowRateIndicator) {
        registerRefreshables(volumetricFlowRateIndicator);
    }

    public void refresh() {
        this.refreshables.forEach(Refreshable::refresh);
        makeTimeLeftPrediction();
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
        refreshDataModule();
        refreshTemperatureSlider();
        enableTemperatureSlider();
        enableStartButton();
        enableSettingsButton();
        disableStopButton();
    }

    public void makeTimeLeftPrediction() {
        List<Double> colderRoom = dataModule.getInitialColderRoomTemperatures();
        int size = colderRoom.size();
        int counter = 1;
        while (counter < size && pack(colderRoom.get(counter)) <= pack(desiredTemperature.get())) {
            incrementElapsedTimeCounter();
            counter++;
        }
        setElapsedTimeIndicatorValue();
    }

    public void prepareAnimation() {
        double speed = dataModule.getSpeed();

        List<Double> roomATemp = dataModule.getRoomATemperatures();
        List<Double> roomBTemp = dataModule.getRoomBTemperatures();

        int limit = elapsedTimeCounter.getTimeLeft();

        for (int timePoint = 1; timePoint <= limit; timePoint++) {
            double roomATemperature = roomATemp.get(timePoint);
            double roomBTemperature = roomBTemp.get(timePoint);
            int timePointCopy = timePoint;

            temperaturesChartIndicator.getAnimation().getKeyFrames().add(
                    new KeyFrame(Duration.millis(timePoint * 1000 * speed), (ActionEvent e) -> {
                        temperatureAchievedCallback(timePointCopy, roomATemperature, roomBTemperature);
                        setInitialTemperatures(roomATemperature, roomBTemperature);
                        decrementElapsedTimeCounter();
                        setElapsedTimeIndicatorValue();
                    })
            );
        }
        temperaturesChartIndicator.getAnimation().setOnFinished((ActionEvent event) -> finishAnimation());
        temperaturesChartIndicator.setTimeUpperBound(elapsedTimeCounter.getTimeLeft());
    }

    private void setInitialTemperatures(double roomATemperature, double roomBTemperature) {
        dataModule.setRoomAInitialTemperature(roomATemperature);
        dataModule.setRoomBInitialTemperature(roomBTemperature);
    }

    private void setElapsedTimeIndicatorValue() {
        elapsedTimeIndicator.setElapsedTime(elapsedTimeCounter.getTimeLeft());
    }

    private void incrementElapsedTimeCounter() {
        elapsedTimeCounter.increment();
    }

    private void decrementElapsedTimeCounter() {
        elapsedTimeCounter.decrement();
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
        roomATemperature.setText(String.valueOf(pack(temperature)));
    }

    public void setRoomBTemperatureIndicatorValue(double temperature) {
        roomBTemperature.setText(String.valueOf(pack(temperature)));
    }

    public void setDesiredTemperatureIndicatorValue(double temperature) {
        desiredTemperatureIndicator.setText(String.valueOf(pack(temperature)));
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

    public void refreshLeftTimeCounter() {
        elapsedTimeCounter.refresh();
    }

    private void refreshDataModule() {
        dataModule.refresh();
    }

}
