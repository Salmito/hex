package com.salmito.hex.engine.things;

import com.salmito.hex.engine.Thing;

/**
 * Created by tiago on 8/13/2015.
 */
public class Point implements Thing {
    private float x,y,z;

    public Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(float[] coordinates) {
        this(coordinates[0],coordinates[1],coordinates[2]);
    }

    @Override
    public void draw(long time) {

    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public void clean() {

    }
}
