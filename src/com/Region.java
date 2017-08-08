package com;


public class Region {
    public int regionNumber;
    public int startX;
    public int startY;
    public int endX;
    public int endY;

    @Override
    public String toString() {
        return "Region{" +
                "startX=" + startX +
                ", startY=" + startY +
                ", endX=" + endX +
                ", endY=" + endY +
                '}';
    }
}
