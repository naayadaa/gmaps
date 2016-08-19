package com.naya;

/**
 * Created by naayadaa on 19.08.16.
 */
public class Rect {
    private double top;
    private double bottom;
    private double left;
    private double right;
    private double width;
    private double height;
    private double midWidth;
    private double midHeight;

    public Rect( double left, double top, double width, double height) {
        this.top = top;
        this.left = left;
        this.right = left + width;
        this.width = width;
        this.bottom = top + height;
        this.height = height;
        midWidth = width/2 + left;
        midHeight = height/2 + bottom;
    }

    public double getTop() {
        return top;
    }

    public double getBottom() {
        return bottom;
    }

    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }

    public double getMidWidth() {
        return midWidth;
    }

    public double getMidHeight() {
        return midHeight;
    }

    public double diagDistance(){
        //pythagarus
        return Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
    }
}
