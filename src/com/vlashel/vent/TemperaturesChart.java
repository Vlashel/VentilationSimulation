package com.vlashel.vent;

import javafx.animation.Timeline;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class TemperaturesChart extends LineChart<Number, Number> {
    private XYChart.Series<Number, Number> roomASeries;
    private XYChart.Series<Number, Number> roomBSeries;
    private Timeline timeline;
    private double xUpperBound;
    private double yUpperBound;

    public TemperaturesChart() {
        super(new NumberAxis(), new NumberAxis());

        this.getStylesheets().add(getClass().getResource("css/stylesheets.css").toExternalForm());
        this.setCreateSymbols(false);

        NumberAxis xAxis = (NumberAxis) this.getXAxis();
        xAxis.setUpperBound(xUpperBound);
        xAxis.setAutoRanging(false);
        xAxis.setLabel("Time in seconds");

        NumberAxis yAxis = (NumberAxis) this.getYAxis();
        yAxis.setUpperBound(yUpperBound);
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

    public void setxUpperBound(double xUpperBound) {
        this.xUpperBound = xUpperBound;
    }

    public void setyUpperBound(double yUpperBound) {
        this.yUpperBound = yUpperBound;
    }
}
