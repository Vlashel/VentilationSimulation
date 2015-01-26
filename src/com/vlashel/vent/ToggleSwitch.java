package com.vlashel.vent;

import javafx.beans.binding.Bindings;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author vshel
 * @version 1.0
 * @since 1/13/2015
 */
public class ToggleSwitch extends ToggleButton {
    public ToggleSwitch(double translateX, double translateY) {
        this.setTranslateX(translateX);
        this.setTranslateY(translateY);
        this.setSelected(true);
        this.setMaxSize(1, 1);
        this.setMinSize(1, 1);

        Image selected = new Image(Ventilator.class.getResource("images/on-button.png").toExternalForm());
        Image unselected = new Image(Ventilator.class.getResource("images/off-button.png").toExternalForm());

        ImageView toggleImage = new ImageView();
        this.setGraphic(toggleImage);
        toggleImage.imageProperty().bind(Bindings
                        .when(this.selectedProperty())
                        .then(selected)
                        .otherwise(unselected)
        );
    }
}
