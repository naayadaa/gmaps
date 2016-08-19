package com.naya;

/**
 * Created by naayadaa on 19.08.16.
 */
public enum StartPosition {

    HOME(0), BOTTOM_LEFT(1), TOP_LEFT(2), BOTTOM_RIGHT(3),
    TOP_RIGHT(4), POINT(5);

    private final int i;

    StartPosition(int i){
        this.i = i;
    }

    public int getI() {
        return i;
    }
}
