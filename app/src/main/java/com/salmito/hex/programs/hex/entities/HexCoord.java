package com.salmito.hex.programs.hex.entities;

import android.util.Log;

import com.salmito.hex.engine.things.geometry.Point2f;

import static java.lang.Math.abs;

/**
 * Created by Tiago on 16/09/2015.
 */
public class HexCoord {
    private static float sq = (float) Math.sqrt(3.0);
    private static float H = Hexagon.radius * (float) Math.sin(Math.PI / 6);
    private static float height = Hexagon.radius + 2.0f * H;
    private static float R = Hexagon.radius * (float) Math.cos(Math.PI / 6);
    private static float M = H / R;
    private static float width = 2.0f * R;

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

    private HexCoord neighbor(int direction) {
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
        this(x, y);
    }

    public static HexCoord get(int x, int y, int z) {
        return new HexCoord(x + (z - (z & 1)) / 2, z);
    }

    public static HexCoord geo(float x, float y) {

        x += R;
        //else x-=R;
        y += Hexagon.radius;
        //else y-=Hexagon.radius;

        int Xs = (int) (x / width); //hex coord
        int Ys = (int) (y / (H + Hexagon.radius));

        float Xi = abs(x - ((float) Xs) * width); //position inside hex
        float Yi = y - abs(((float) Ys) * (H + Hexagon.radius));

        if (Ys % 2 == 1) { //even line B
            if (Xi >= R) {
                if (Yi < (2 * H - Xi * M)) Ys--;
            } else {
                if (Yi < (Xi * M)) Ys--;
                else Xs--;
            }
        } else { //odd line A
            if (Yi < H - Xi * M) {
                Ys--;
                Xs--;
            } else if (Yi < (-H + Xi * M)) Ys--;
        }
        //if(Ys<0) Ys=0;
        //if(Xs<0) Xs=0;

        HexCoord hexCoord = new HexCoord(Xs, Ys);
        Log.d("HexCoord", "Getting Coord from geo [" + x + ", " + y + "] -> " + hexCoord);
        return hexCoord;
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
        return r;
    }

    public int getZ() {
        return -r - q;
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
        return new HexCoord(a.q + b.q, a.r + b.r, a.getZ() + b.getZ());
    }

    HexCoord subtract(HexCoord a, HexCoord b) {
        return new HexCoord(a.q - b.q, a.r - b.r, a.getZ() - b.getZ());
    }

    HexCoord multiply(HexCoord a, int k) {
        return new HexCoord(a.q * k, a.r * k, a.getZ() * k);
    }

    public static HexFractionalCoord geo_to_hex(float x, float y, Layout layout) {
        HexOrientation M = layout.getOrientation();
        Point2f pt = new Point2f((x - layout.getOrigin().getX()) / layout.getSize().getX(),
                (y - layout.getOrigin().getY()) / layout.getSize().getY());
        float q = M.b0 * pt.getX() + M.b1 * pt.getY();
        float r = M.b2 * pt.getX() + M.b3 * pt.getY();
        return new HexFractionalCoord(q, r , -q -r);
    }

    public Point2f to_geo(Layout layout) {
        HexOrientation M = layout.getOrientation();
        float x = (M.f0 * q + M.f1 * r) * layout.getSize().getX();
        float y = (M.f2 * q + M.f3 * r) * layout.getSize().getY();
        return new Point2f(x + layout.getOrigin().getX(),
                y + layout.getOrigin().getY());
    }

}
