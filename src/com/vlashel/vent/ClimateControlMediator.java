package com.vlashel.vent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vshel
 * @version 1.0
 * @since 1/26/2015.
 */
public class ClimateControlMediator {
    List<RoomClimateControl> controls = new ArrayList<>();

    public void addControl(RoomClimateControl control) {
        this.controls.add(control);
    }

    public void send(int temperature, RoomClimateControl control) {
        for (RoomClimateControl c : controls) {
            if (!c.equals(control)) {
                c.receive(temperature, control);
            }
        }
    }

    public void sendTempAchieved(RoomClimateControl control) {
        for (RoomClimateControl c : controls) {
            if (!c.equals(control)) {
                c.setNormalRotation();
            }
        }
    }

}
