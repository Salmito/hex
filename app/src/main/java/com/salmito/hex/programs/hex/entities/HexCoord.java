package com.salmito.hex.programs.hex.entities;

import com.salmito.hex.engine.things.geometry.Point2f;

import static java.lang.Math.abs;

/**
 * Created by Tiago on 16/09/2015.
 */
public class HexCoord {
    private final int hash;
    private final int q;
    private final int r;


    private static HexCoord[] directions = {
            new HexCoord(1, 0),
            new HexCoord(1, -1),
            new HexCoord(0, -1),
            new HexCoord(-1, 0),
            new HexCoord(-1, 1),
            new HexCoord(0, 1)
    };

    private static HexCoord direction(int direction /* 0 to 5 */) {
        return directions[direction%6];
    }

    public HexCoord neighbor(int direction) {
        return add(this, direction(direction));
    }


    public HexCoord getDirection(int direction) {
        return new HexCoord(
                getQ() + directions[direction].getQ(),
                getR() + directions[direction].getR());
    }

    public HexCoord(int q, int r) {
        this.q = q;
        this.r = r;

        int sum = q + r;
        hash = sum * (sum + 1) / 2 + r;
    }

    public HexCoord(int x, int y, int z) {
        this(x, z);
    }

    public int getQ() {
        return q;
    }

    public int getR() {
        return r;
    }

    public int getX() {
        return q;
    }

    public int getY() {
        return -r-q;
    }

    public int getZ() {
        return r;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof HexCoord) {
            HexCoord t = (HexCoord) o;
            return this.q == t.q && this.r == t.r;
        }
        return false;
    }


    @Override
    public int hashCode() {
        return hash;
    }

    public int length() {
        return (abs(q) + abs(r) + abs(getZ()) / 2);
    }

    public int distance(HexCoord p) {
         return subtract(this, p).length();
    }

    public HexCoord[] getLine(HexCoord b) {
        int N = distance(b);
        HexCoord[] ret = new HexCoord[N + 1];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = HexFractionalCoord.lerp(this, b, (1f / N) * i).round();
        }
        return ret;
    }

    @Override
    public String toString() {
        return "Coordinates[qr=" + getQ() + "," + getR() + " xyz=" + getX() + "," + getY() + "," + getZ() + "]";
    }

    HexCoord add(HexCoord a, HexCoord b) {
        return new HexCoord(a.getX() + b.getX(), a.getY() + b.getY(), a.getZ() + b.getZ());
    }

    HexCoord subtract(HexCoord a, HexCoord b) {
        return new HexCoord(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
    }

    HexCoord multiply(HexCoord a, int k) {
        return new HexCoord(a.getX() * k, a.getY() * k, a.getZ() * k);
    }

    public static HexCoord geo_to_hex(float x, float y, HexLayout layout) {
        HexOrientation M = layout.getOrientation();
        Point2f pt = new Point2f((x - layout.getOrigin().getX()) / layout.getSize().getX(),
                (y - layout.getOrigin().getY()) / layout.getSize().getY());
        float q = M.b0 * pt.getX() + M.b1 * pt.getY();
        float r = M.b2 * pt.getX() + M.b3 * pt.getY();
        return new HexFractionalCoord(q, -q -r, r).round();
    }

    public Point2f to_geo(HexLayout layout) {
        HexOrientation M = layout.getOrientation();
        float x = (M.f0 * q + M.f1 * r) * layout.getSize().getX();
        float y = (M.f2 * q + M.f3 * r) * layout.getSize().getY();
        return new Point2f(x + layout.getOrigin().getX(),
                y + layout.getOrigin().getY());
    }

}
