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

public class ControllerMediator implements Refreshable {
    private DataModule dataModule;

    private StartButton startButton;
    private StopButton stopButton;
    private SettingsButton settingsButton;
    private TemperatureSliderInput temperatureSliderInput;
    private TemperaturesChartIndicator temperaturesChartIndicator;
    private ServerRoomTemperatureIndicator roomATemperature;
    private OfficeRoomTemperatureIndicator roomBTemperature;
    private DesiredTemperatureIndicator desiredTemperatureIndicator;
    private TimeLeftIndicator timeLeftIndicator;
    private SettingsWindow settingsWindow;
    private List<Ventilator> serverRoomVentilators = new ArrayList<>();
    private List<Ventilator> officeRoomVentilators = new ArrayList<>();
    private ElapsedTimeCounter elapsedTimeCounter = new ElapsedTimeCounter();
    private List<Refreshable> refreshables = new ArrayList<>();

    private DoubleProperty desiredTemperature = new SimpleDoubleProperty();
    private BooleanProperty recuperate = new SimpleBooleanProperty();

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

    private double speed;

    private int timePoint;
    private int dt;


    public ControllerMediator(DataModule dataModule) {
        this.dataModule = dataModule;

        desiredTemperature.bind(dataModule.desiredTemperatureProperty());
        recuperate.bind(dataModule.recuperateProperty());

        init();
    }

    private void init() {
        elapsedTimeCounter = new ElapsedTimeCounter();

        serverRoomTemperature = dataModule.getServerRoomTemperature();
        officeRoomTemperature = dataModule.getOfficeRoomTemperature();

        volumetricFlowRate = dataModule.getVolumetricFlowRate();
        serverRoomVolume = dataModule.getServerRoomVolume();
        officeRoomVolume = dataModule.getOfficeRoomVolume();

        serverRoomTemperatureMax = dataModule.getServerRoomTemperatureMax();
        serverRoomTemperatureMin = dataModule.getServerRoomTemperatureMin();
        outsideAirTemperature = dataModule.getOutsideAirTemperature();
        serverRoomTemperatureIncrease = dataModule.getServerRoomTemperatureIncrease();
        officeRoomTemperatureDecrease = dataModule.getOfficeRoomTemperatureDecrease();
        precision = dataModule.getPrecision();
        speed = dataModule.getSpeed();
        dt = dataModule.getDt();

        timePoint = 0;
    }

    private void refreshSelf() {
        init();
    }

    public void registerServerRoomVentilators(Ventilator... ventilators) {
        Arrays.asList(ventilators).forEach(this.serverRoomVentilators::add);
    }

    public void registerOfficeRoomVentilators(Ventilator... ventilators) {
        Arrays.asList(ventilators).forEach(this.officeRoomVentilators::add);
    }

    public void registerTemperatureSlider(TemperatureSliderInput temperatureSliderInput) {
        this.temperatureSliderInput = temperatureSliderInput;
        dataModule.desiredTemperatureProperty().bind(temperatureSliderInput.valueProperty());
        registerRefreshables(temperatureSliderInput);
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

    public void registerElapsedTimeIndicator(TimeLeftIndicator timeLeftIndicator) {
        this.timeLeftIndicator = timeLeftIndicator;
        registerRefreshables(timeLeftIndicator);

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

    public void registerServerRoomVolumeIndicator(ServerRoomVolumeIndicator serverRoomVolumeIndicator) {
        registerRefreshables(serverRoomVolumeIndicator);
    }

    public void registerOfficeRoomVolumeIndicator(OfficeRoomVolumeIndicator officeRoomVolumeIndicator) {
        registerRefreshables(officeRoomVolumeIndicator);
    }

    public void registerVolumetricFlowRateIndicator(VolumetricFlowRateIndicator volumetricFlowRateIndicator) {
        registerRefreshables(volumetricFlowRateIndicator);
    }

    @Override
    public void refresh() {
        this.refreshables.forEach(Refreshable::refresh);
        refreshSelf();
    }

    public void makeTimeLeftPrediction() {
        elapsedTimeCounter.setElapsedTime(dataModule.simulateAndGetTimeLeft());
        setElapsedTimeIndicatorValue();
    }

    private void setInitialTemperatures(double serverRoomTemperature, double officeRoomTemperature) {
        dataModule.setServerRoomTemperature(serverRoomTemperature);
        dataModule.setOfficeRoomTemperature(officeRoomTemperature);
    }

    private void setElapsedTimeIndicatorValue() {
        timeLeftIndicator.setElapsedTime(elapsedTimeCounter.getElapsedTime());
    }

    private void incrementElapsedTimeCounter() {
        elapsedTimeCounter.increment();
    }

    private void decrementElapsedTimeCounter() {
        elapsedTimeCounter.decrement();
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


    public void control() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                possiblyAdjustChart();

                while (alwaysTrue()) {
                    if (shouldRecuperateNow()
                            && pack(serverRoomTemperature) == serverRoomTemperatureMax
                            && pack(officeRoomTemperature) < pack(desiredTemperature.get())) {

                        Platform.runLater(() -> makeTimeLeftPrediction());
                        officeRoomVentilators.forEach(Ventilator::play);

                        while (pack(officeRoomTemperature) <= pack(desiredTemperature.get())
                                && pack(serverRoomTemperature) <= serverRoomTemperatureMax
                                && pack(serverRoomTemperature) >= serverRoomTemperatureMin
                                && recuperate.get()) {

                            Platform.runLater(() -> possiblyAdjustChart());

                            double dTofficedt = ((volumetricFlowRate / officeRoomVolume) * (serverRoomTemperature - officeRoomTemperature)) - officeRoomTemperatureDecrease;
                            double dTserverdt = ((volumetricFlowRate / serverRoomVolume) * (officeRoomTemperature - serverRoomTemperature)) + serverRoomTemperatureIncrease;

                            officeRoomTemperature += dTofficedt * dt;
                            serverRoomTemperature += dTserverdt * dt;

                            Platform.runLater(() -> callback());

                            sleep();
                        }

                        officeRoomVentilators.forEach(Ventilator::stop);
                    } else {
                        if (pack(serverRoomTemperature) >= serverRoomTemperatureMax && !shouldRecuperateNow()) {
                            serverRoomVentilators.forEach(Ventilator::play);

                            while (pack(serverRoomTemperature) >= serverRoomTemperatureMin && !shouldRecuperateNow()) {
                                Platform.runLater(() -> possiblyAdjustChart());

                                double dTserverdt = ((volumetricFlowRate / serverRoomVolume) * (outsideAirTemperature - serverRoomTemperature)) + serverRoomTemperatureIncrease;

                                serverRoomTemperature += dTserverdt * dt;

                                if (pack(officeRoomTemperature) > outsideAirTemperature) {
                                    officeRoomTemperature -= officeRoomTemperatureDecrease * dt;
                                }

                                Platform.runLater(() -> callback());

                                sleep();
                            }

                            serverRoomVentilators.forEach(Ventilator::stop);
                        } else {
                            Platform.runLater(() -> possiblyAdjustChart());

                            serverRoomTemperature += serverRoomTemperatureIncrease * dt;
                            if (pack(officeRoomTemperature) > outsideAirTemperature) {
                                officeRoomTemperature -= officeRoomTemperatureDecrease * dt;
                            }

                            Platform.runLater(() -> callback());

                            sleep();
                        }
                    }
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

    private void callback() {

        temperaturesChartIndicator.plot(
                timePoint += dt,
                serverRoomTemperature,
                officeRoomTemperature
        );

        setInitialTemperatures(serverRoomTemperature, officeRoomTemperature);

        setRoomATemperatureIndicatorValue(serverRoomTemperature);
        setRoomBTemperatureIndicatorValue(officeRoomTemperature);

        if (recuperate.get()) {
            if (elapsedTimeCounter.getElapsedTime() != 0) {
                decrementElapsedTimeCounter();
                setElapsedTimeIndicatorValue();
            } else {
                setElapsedTimeIndicatorValue();
            }
        } else {
            setElapsedTimeIndicatorValue();
        }
        if (pack(desiredTemperature.get()) > officeRoomTemperature) {
            refreshTemperatureSlider();
        }
    }

    private boolean shouldRecuperateNow() {
        return recuperate.get() && pack(desiredTemperature.get()) - pack(officeRoomTemperature) >= precision;
    }

    private void possiblyAdjustChart() {
        if (timePoint % 1000 == 0) {
            temperaturesChartIndicator.setTimeLowerBound(timePoint);
            temperaturesChartIndicator.setTimeUpperBound(timePoint + 1000);
        }
    }

    public void enableDesiredTemperatureIndicator() {
        this.desiredTemperatureIndicator.enable();
    }

    public void disableDesiredTemperatureIndicator() {
        this.desiredTemperatureIndicator.disable();
    }

    public void enableTimeLeftIndicator() {
        this.timeLeftIndicator.enable();
    }

    public void disableTimeLeftIndicator() {
        this.timeLeftIndicator.disable();
    }

    private void sleep() throws InterruptedException {
        Thread.sleep((long) (1000 * dt * speed));
    }

    public void setRecuperate(boolean recuperate) {
        this.dataModule.setRecuperate(recuperate);
    }

}
