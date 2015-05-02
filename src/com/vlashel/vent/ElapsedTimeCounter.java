package com.vlashel.vent;

/**
 * @author Vlashel
 * @version 1.0
 * @since 02.05.2015.
 */
public class ElapsedTimeCounter {
    private int elapsedTime = 0;

    public void increment() {
        elapsedTime++;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

}
