package com.vlashel.vent;

import javafx.animation.Timeline;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class TemperaturesChart extends LineChart<Number, Number> implements Animatable, Refreshable {
    private XYChart.Series<Number, Number> roomASeries;
    private XYChart.Series<Number, Number> roomBSeries;
    private Timeline timeline;
    private DataModule dataModule;

    public TemperaturesChart(DataModule dataModule) {
        super(new NumberAxis(), new NumberAxis());

        this.dataModule = dataModule;
        this.getStylesheets().add(getClass().getResource("css/stylesheets.css").toExternalForm());
        this.setCreateSymbols(false);

        NumberAxis xAxis = (NumberAxis) this.getXAxis();
        xAxis.setUpperBound(dataModule.getTotalTime());
        xAxis.setLabel("Time in seconds");

        NumberAxis yAxis = (NumberAxis) this.getYAxis();
        yAxis.setUpperBound(Math.floor(dataModule.getHighestTemperature()) + 10);
        yAxis.setAutoRanging(false);
        yAxis.setLabel("Temperature in Celsius");

        roomASeries = new XYChart.Series<>();
        roomBSeries = new XYChart.Series<>();

        roomASeries.setName("Room A");
        roomBSeries.setName("Room B");

        this.setAnimated(false);
        this.setTitle("Temperatures change over time");

        init();
    }

    private void init() {
        this.getData().add(roomASeries);
        this.getData().add(roomBSeries);
        timeline = new Timeline();
    }

    public void plot(int timePoint, double roomATemperature, double roomBTemperature) {
        roomASeries.getData().add(new Data<>(timePoint, roomATemperature));
        roomBSeries.getData().add(new Data<>(timePoint, roomBTemperature));
    }

    @Override
    public void refresh() {
        roomASeries.getData().clear();
        roomBSeries.getData().clear();
        this.getData().clear();
        this.getData().clear();

        plot(0, dataModule.getRoomATemperatures()[0], dataModule.getRoomBTemperatures()[0]);
        init();
    }

    public Timeline getAnimation() {
        return timeline;
    }

    @Override
    public void play() {
        timeline.play();
    }

    @Override
    public void stop() {
        timeline.stop();
    }
}
