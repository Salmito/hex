package com.salmito.hex.engine.things.geometry;

import com.salmito.hex.engine.Thing;
import com.salmito.hex.util.Constants;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by tiago on 8/13/2015.
 */
public class Point3f implements Thing {
    private static final DecimalFormat format = new DecimalFormat("#.####", new DecimalFormatSymbols(Locale.US)); //NOI18N
    private final FloatBuffer buffer;
    private int offset;

    public Point3f(float x, float y, float z) {
        this.buffer = ByteBuffer.allocateDirect(3 * Constants.bytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer().put(new float[]{x, y, z});
        this.offset = 0;
    }

    public Point3f(FloatBuffer buf, int offset) {
        float[] dst = new float[3];
        buffer = buf;
        this.offset = offset;
    }

    public Point3f(float[] coordinates) {
        this(coordinates[0], coordinates[1], coordinates[2]);
    }

    public Point3f(Point3f o) {
        this(o.getX(), o.getY(), o.getZ());
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public FloatBuffer getBuffer() {
        return buffer;
    }

    public float[] getCoords() {
        float[] ret = new float[3];
        buffer.get(ret, offset, 3);
        return ret;
    }

    @Override
    public void draw(long time) {

    }

    public float getX() {
        return buffer.get(offset);
    }

    public void setX(float x) {
        buffer.put(offset, x);
    }

    public float getY() {
        return buffer.get(offset + 1);
    }

    public void setY(float y) {
        buffer.put(offset + 1, y);
    }

    public float getZ() {
        return buffer.get(offset + 2);
    }

    public void setZ(float z) {
        buffer.put(offset + 2, z);
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
        return String.format(this.getClass().getSimpleName()+"[x=%s, y=%s, z=%s]", format.format(getX()), format.format(getY()), format.format(getZ()));
    }

    public void set(Point3f p) {
        setX(p.getX());
        setY(p.getY());
        setZ(p.getZ());
    }
}
