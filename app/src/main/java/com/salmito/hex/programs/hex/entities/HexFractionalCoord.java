package com.salmito.hex.programs.hex.entities;

import android.util.Log;

import com.salmito.hex.engine.things.geometry.Point2f;

import static java.lang.Math.abs;

/**
 * Created by Tiago on 16/09/2015.
 */
public class HexFractionalCoord {
    private final float q;
    private final float r;
    private final float s;

    public HexFractionalCoord(float x, float y, float z) {
        r=x;
        q=y;
        s=z;
    }

    public HexCoord round() {
        float rx = Math.round(r), ry = Math.round(q), rz = Math.round(s);
        float x_diff = abs(rx - r), y_diff = abs(ry - q), z_diff = abs(rz - s);
        if (x_diff > y_diff && x_diff > z_diff)
            rx = -ry - rz;
        else if (y_diff > z_diff)
            ry = -rx - rz;
        else
            rz = -rx - ry;
        return new HexCoord((int) rx, (int) ry, (int) rz);
    }

    static public HexFractionalCoord lerp(HexCoord a, HexCoord b, float t) {
        return new HexFractionalCoord(
                a.getX() + (b.getX() - a.getX()) * t,
                a.getY() + (b.getY() - a.getY()) * t,
                a.getZ() + (b.getZ() - a.getZ()) * t);
    }
}
