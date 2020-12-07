package com.example.bme3890projectapp;

public class Point {

    public float dy;

    public float dx;
    float x, y;

    @Override
    public String toString() {

        return x + ", " + y;

    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }
}
