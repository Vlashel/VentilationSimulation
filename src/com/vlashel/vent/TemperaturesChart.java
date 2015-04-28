package com.vlashel.vent;

import javafx.animation.Timeline;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class TemperaturesChart extends LineChart<Number, Number> implements Animatable{
    private XYChart.Series<Number, Number> roomASeries;
    private XYChart.Series<Number, Number> roomBSeries;
    private Timeline timeline;
    private DataModule dataModule;

    public TemperaturesChart(DataModule dataModule) {
        super(new NumberAxis(), new NumberAxis());

        this.getStylesheets().add(getClass().getResource("css/stylesheets.css").toExternalForm());
        this.setCreateSymbols(false);

        NumberAxis xAxis = (NumberAxis) this.getXAxis();
        xAxis.setUpperBound(dataModule.getTotalTime());
        xAxis.setAutoRanging(false);
        xAxis.setLabel("Time in seconds");

        NumberAxis yAxis = (NumberAxis) this.getYAxis();
        yAxis.setUpperBound(dataModule.getRoomATemperatures()[0] + 5);
        yAxis.setAutoRanging(false);
        yAxis.setLabel("Temperature in Celsius");

        timeline = new Timeline();
        this.setAnimated(false);
        this.setTitle("Temperatures change over time");

        roomASeries = new XYChart.Series<>();
        roomBSeries = new XYChart.Series<>();

        roomASeries.setName("Room A");
        roomBSeries.setName("Room B");

        this.getData().add(roomASeries);
        this.getData().add(roomBSeries);
    }

    public void plot(double timePoint, double roomATemperature, double roomBTemperature) {
        roomASeries.getData().add(new XYChart.Data<>(timePoint, roomATemperature));
        roomBSeries.getData().add(new XYChart.Data<>(timePoint, roomBTemperature));
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
