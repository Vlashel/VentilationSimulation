package com.vlashel.vent;

import com.vlashel.vent.button.SettingsButton;
import com.vlashel.vent.button.StartButton;
import com.vlashel.vent.button.StopButton;
import com.vlashel.vent.indicator.*;
import com.vlashel.vent.input.TemperatureSliderInput;
import com.vlashel.vent.window.AlertBoxWindow;
import com.vlashel.vent.window.SettingsWindow;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.vlashel.vent.Helper.*;

public class ControllerMediator {
    private StartButton startButton;
    private StopButton stopButton;
    private SettingsButton settingsButton;
    private TemperatureSliderInput temperatureSliderInput;
    private DataModule dataModule;
    private TemperaturesChartIndicator temperaturesChartIndicator;
    private ServerRoomTemperatureIndicator roomATemperature;
    private OfficeRoomTemperatureIndicator roomBTemperature;
    private DesiredTemperatureIndicator desiredTemperatureIndicator;
    private ElapsedTimeIndicator elapsedTimeIndicator;
    private SettingsWindow settingsWindow;
    private List<Ventilator> serverRoomVentilators = new ArrayList<>();
    private List<Ventilator> officeRoomVentilators = new ArrayList<>();
    private ElapsedTimeCounter elapsedTimeCounter = new ElapsedTimeCounter();
    private List<Refreshable> refreshables = new ArrayList<>();
    private List<Animatable> animatables = new ArrayList<>();
    private int timeLeft = 0;

    private DoubleProperty desiredTemperature = new SimpleDoubleProperty();
    private BooleanProperty isRecuperationOn = new SimpleBooleanProperty();

    private Double serverRoomTemperature;
    private Double officeRoomTemperature;
    private double volumetricFlowRate;
    private double serverRoomVolume;
    private double officeRoomVolume;
    private double serverRoomTempMax = 27.0;
    private double serverRoomTempMin = 22.0;
    private double outsideAirTemp = 14.0;
    private double serverRoomTempIncrease = 0.02598786;
    private double officeRoomTempDecrease = 0.001;
    private long millis = 100;
    private int timePoint = 0;

    public ControllerMediator() {
        init();

        serverRoomTemperature = 27.0;
        officeRoomTemperature = 18.0;
        // initial temperature in Celsius
        // initial temperature in Celsius
        volumetricFlowRate = 0.15; // volumetric flow rate in cubic meters per second
        serverRoomVolume = 30.0; // cubic meters 3 x 4 x 2.5
        officeRoomVolume = 140.0; // cubic meters 8 x 7 x 2.5
    }

    private void init() {
        elapsedTimeCounter = new ElapsedTimeCounter();
    }

    public void registerServerRoomVentilators(Ventilator... ventilators) {
        registerAnimatables(ventilators);
        Arrays.asList(ventilators).forEach(this.serverRoomVentilators::add);
    }

    public void registerOfficeRoomVentilators(Ventilator... ventilators) {
        registerAnimatables(ventilators);
        Arrays.asList(ventilators).forEach(this.officeRoomVentilators::add);
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

    private boolean alwaysTrue() {
        return true;
    }

    public void registerRoomATemperatureIndicator(ServerRoomTemperatureIndicator roomATemperature) {
        this.roomATemperature = roomATemperature;
        registerRefreshables(roomATemperature);
    }

    public void registerRoomBTemperatureIndicator(OfficeRoomTemperatureIndicator roomBTemperature) {
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

    public void registerRoomAVolumeIndicator(ServerRoomVolumeIndicator serverRoomVolumeIndicator) {
        registerRefreshables(serverRoomVolumeIndicator);
    }

    public void registerRoomBVolumeIndicator(OfficeRoomVolumeIndicator officeRoomVolumeIndicator) {
        registerRefreshables(officeRoomVolumeIndicator);
    }

    public void registerVolumetricFlowRateIndicator(VolumetricFlowRateIndicator volumetricFlowRateIndicator) {
        registerRefreshables(volumetricFlowRateIndicator);
    }

    public void refresh() {
        this.refreshables.forEach(Refreshable::refresh);
    }

    public void makeTimeLeftPrediction() {
        refreshElapsedTimeCounter();
        elapsedTimeCounter.setTimeLeft(dataModule.simulateAndGetTimeLeft());
        Platform.runLater(() -> setElapsedTimeIndicatorValue());
    }

    private void setInitialTemperatures(double serverRoomTemperature, double officeRoomTemperature) {
        dataModule.setServerRoomTemperature(serverRoomTemperature);
        dataModule.setOfficeRoomTemperature(officeRoomTemperature);
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

    private void callback(int timePoint,
                          double serverRoomTemperature,
                          double officeRoomTemperature) {

        temperaturesChartIndicator.plot(
                timePoint,
                serverRoomTemperature,
                officeRoomTemperature
        );

        setInitialTemperatures(serverRoomTemperature, officeRoomTemperature);

        setRoomATemperatureIndicatorValue(serverRoomTemperature);
        setRoomBTemperatureIndicatorValue(officeRoomTemperature);

        if (isRecuperationOn.get()) {
            decrementElapsedTimeCounter();
            setElapsedTimeIndicatorValue();
        }
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

    public void refreshElapsedTimeCounter() {
        elapsedTimeCounter.refresh();
    }

    private void refreshDataModule() {
        dataModule.refresh();
    }

    public boolean getIsRecuperationOn() {
        return isRecuperationOn.get();
    }

    public BooleanProperty isRecuperationOnProperty() {
        return isRecuperationOn;
    }

    public void setIsRecuperationOn(boolean isRecuperationOn) {
        this.isRecuperationOn.set(isRecuperationOn);
    }

    public void animate() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int dt = 1;
                possiblyAdjustChart();

                while (alwaysTrue()) {
                    if (getIsRecuperationOn()) {
                        makeTimeLeftPrediction();
                    }
                    if (checkWhetherShouldRecuperateNow() && pack(serverRoomTemperature) >= serverRoomTempMax) {
                        officeRoomVentilators.forEach(Ventilator::play);

                        while (pack(officeRoomTemperature) <= pack(desiredTemperature.get()) && pack(serverRoomTemperature) <= serverRoomTempMax) {
                            possiblyAdjustChart();

                            double dTofficedt = ((volumetricFlowRate / officeRoomVolume) * (serverRoomTemperature - officeRoomTemperature)) - officeRoomTempDecrease;
                            double dTserverdt = ((volumetricFlowRate / serverRoomVolume) * (officeRoomTemperature - serverRoomTemperature)) + serverRoomTempIncrease;

                            officeRoomTemperature += dTofficedt * dt;
                            serverRoomTemperature += dTserverdt * dt;

                            Platform.runLater(() -> callback(timePoint += dt, serverRoomTemperature, officeRoomTemperature));

                            Thread.sleep(millis * dt);
                        }

                        officeRoomVentilators.forEach(Ventilator::stop);
                    } else {
                        if (pack(serverRoomTemperature) >= serverRoomTempMax) {
                            serverRoomVentilators.forEach(Ventilator::play);

                            while (serverRoomTemperature >= serverRoomTempMin && !checkWhetherShouldRecuperateNow()) {
                                possiblyAdjustChart();

                                double dTserverdt = ((volumetricFlowRate / serverRoomVolume) * (outsideAirTemp - serverRoomTemperature)) + serverRoomTempIncrease;

                                serverRoomTemperature += dTserverdt * dt;

                                if (pack(officeRoomTemperature) > outsideAirTemp) {
                                    officeRoomTemperature -= officeRoomTempDecrease * dt;
                                }

                                Platform.runLater(() -> callback(timePoint += dt, serverRoomTemperature, officeRoomTemperature));

                                Thread.sleep(millis * dt);
                            }

                            serverRoomVentilators.forEach(Ventilator::stop);
                        } else {
                            possiblyAdjustChart();

                            serverRoomTemperature += serverRoomTempIncrease * dt;
                            if (pack(officeRoomTemperature) > outsideAirTemp) {
                                officeRoomTemperature -= officeRoomTempDecrease * dt;
                            }

                            Platform.runLater(() -> callback(timePoint += dt, serverRoomTemperature, officeRoomTemperature));
                        }
                    }

                    Thread.sleep(millis * dt);

                }
                return null;
            }
        };

        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return task;
            }
        };
        service.start();
    }

    private boolean checkWhetherShouldRecuperateNow() {
        return getIsRecuperationOn() && pack(desiredTemperature.get()) - pack(officeRoomTemperature) >= 1.0;
    }

    private void possiblyAdjustChart() {
        if (timePoint % 1000 == 0) {
            temperaturesChartIndicator.setTimeLowerBound(timePoint);
            temperaturesChartIndicator.setTimeUpperBound(timePoint + 1000);
        }
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

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }
}
