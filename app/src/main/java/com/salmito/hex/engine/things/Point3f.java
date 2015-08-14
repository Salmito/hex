package com.salmito.hex.engine.things;

import com.salmito.hex.engine.Thing;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by tiago on 8/13/2015.
 */
public class Point3f implements Thing {
    private static final DecimalFormat format = new DecimalFormat("#.####", new DecimalFormatSymbols(Locale.US)); //NOI18N
    private float x, y, z;

    public Point3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3f(float[] coordinates) {
        this(coordinates[0], coordinates[1], coordinates[2]);
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

    /**
     * Computes the square of the distance between this point and point p1.
     *
     * @param p1 the other point
     * @return the square of distance between these two points as a float
     */
    public final float distanceSquared(Point3f p1) {
        double dx = x - p1.x;
        double dy = y - p1.y;
        double dz = z - p1.z;
        return (float) (dx * dx + dy * dy + dz * dz);
    }

    public final float distance(Point3f p1) {
        return (float) Math.sqrt(distanceSquared(p1));
    }

    @Override
    public String toString() {
        return String.format("Point3f[x=%s, y=%s, z=%s]", format.format(x), format.format(y), format.format(z));
    }
}
