package com.vlashel.vent;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;

/**
 * @author Vlashel
 * @version 1.0
 * @since 27.04.2015.
 */
public class TemperaturesChart extends LineChart<Number, Number> {
    private XYChart.Series<Number, Number> roomASeries;
    private XYChart.Series<Number, Number> roomBSeries;
    private Timeline timeline;
    private ComputationModule computationModule;

    public TemperaturesChart(ComputationModule computationModule) {
        super(new NumberAxis(), new NumberAxis());
        this.computationModule = computationModule;

        this.getStylesheets().add(getClass().getResource("css/stylesheets.css").toExternalForm());
        this.setCreateSymbols(false);

        NumberAxis xAxis = (NumberAxis) this.getXAxis();
        xAxis.setUpperBound(computationModule.getTotalTime());
        xAxis.setAutoRanging(false);
        xAxis.setLabel("Time in seconds");

        NumberAxis yAxis = (NumberAxis) this.getYAxis();
        yAxis.setUpperBound(computationModule.getRoomATemperatures()[0] + 5);
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

        prepareAnimation();
        animate();
    }

    public void animate() {
        timeline.play();
    }

    private void plot(double timePoint, int stepIndex) {
        roomASeries.getData().add(new XYChart.Data<>(timePoint, computationModule.getRoomATemperatures()[stepIndex]));
        roomBSeries.getData().add(new XYChart.Data<>(timePoint, computationModule.getRoomBTemperatures()[stepIndex]));
    }

    private void prepareAnimation() {
        double speed = 0.25;

        int stepIndex = 0;
        double timePoint = 0.0;
        double dt = computationModule.getTotalTime() / computationModule.getSteps();

        while (timePoint <= computationModule.getTotalTime()) {
            int stepIndexCopy = stepIndex;
            double timePointCopy = timePoint;

            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(timePoint * 1000 * speed), (ActionEvent e) -> {
                        plot(timePointCopy, stepIndexCopy);
                    })
            );

            if (stepIndex < computationModule.getSteps()) {
                stepIndex++;
            }
            for (double i = 0; i < dt; i++) {
                timePoint++;
            }
        }
    }
}
