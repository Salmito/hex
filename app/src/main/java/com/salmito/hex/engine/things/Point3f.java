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
    private final float[] point;

    public Point3f(float x, float y, float z, float w) {
        this.point = new float[]{x, y, z, w};
    }

    public Point3f(float x, float y, float z) {
        this(x, y, z, 1f);
    }

    public Point3f(float[] coordinates) {
        this(coordinates[0], coordinates[1], coordinates[2], 1f);
    }

    public Point3f(Point3f o) {
        this(o.getX(), o.getY(), o.getZ(), o.getW());
    }

    public float[] getCoords() {
        return point;
    }

    @Override
    public void draw(long time) {

    }

    public float getX() {
        return point[0];
    }

    public float getW() {
        return point[3];
    }

    public void setX(float x) {
        this.point[0] = x;
    }

    public void setW(float w) {
        this.point[3] = w;
    }


    public float getY() {
        return point[1];
    }

    public void setY(float y) {
        this.point[1] = y;
    }

    public float getZ() {
        return point[2];
    }

    public void setZ(float z) {
        point[2] = z;
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
        double dx = getX() - p1.getX();
        double dy = getY() - p1.getY();
        double dz = getZ() - p1.getZ();
        return (float) (dx * dx + dy * dy + dz * dz);
    }

    public final float distance(Point3f p1) {
        return (float) Math.sqrt(distanceSquared(p1));
    }

    @Override
    public String toString() {
        return String.format("Point3f[x=%s, y=%s, z=%s]", format.format(getX()), format.format(getY()), format.format(getZ()));
    }

    public void set(Point3f p) {
        point[0]=p.getX();
        point[1]=p.getY();
        point[2]=p.getZ();
        point[3]=p.getW();
    }
}
