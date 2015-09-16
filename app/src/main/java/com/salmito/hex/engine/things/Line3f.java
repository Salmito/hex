package com.salmito.hex.engine.things;

import com.salmito.hex.engine.Thing;

/**
 * Created by Tiago on 16/09/2015.
 */
public class Line3f implements Thing {

    private final Point3f p1;
    private final Point3f p2;

    public Line3f(Point3f p1, Point3f p2) {
        this.p1=p1;
        this.p2=p2;
    }

    public Point3f interpolate(float t) {
        return interpolate(p1,p2,t);
    }

    /**
     * Interpolate a point along a line
     * @param p1
     * @param p2
     * @param t
     * @return
     */
    public static Point3f interpolate(Point3f p1, Point3f p2, float t) {
        float x=p1.getX()+(p2.getX()-p1.getX())*t;
        float y=p1.getY()+(p2.getY()-p1.getY())*t;
        float z=p1.getZ()+(p2.getZ()-p1.getZ())*t;
        return new Point3f(x,y,z);
    }

    @Override
    public void draw(long time) {

    }

    @Override
    public void clean() {

    }
}
