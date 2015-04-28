package com.vlashel.vent;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ToggleSwitch extends ToggleButton {

    public ToggleSwitch(AnimationMediator animationMediator) {

        this.setOnAction((ActionEvent event) -> {
            if (this.isSelected()) {
                animationMediator.startAnimation();
            } else {
                animationMediator.finishAnimation();
            }
        });

        this.setSelected(false);
        Image selected = new Image(Ventilator.class.getResource("images/off-button.png").toExternalForm());
        Image unselected = new Image(Ventilator.class.getResource("images/on-button.png").toExternalForm());

        ImageView toggleImage = new ImageView();
        this.setGraphic(toggleImage);
        toggleImage.imageProperty().bind(Bindings
                        .when(this.selectedProperty())
                        .then(selected)
                        .otherwise(unselected)
        );
    }
}
