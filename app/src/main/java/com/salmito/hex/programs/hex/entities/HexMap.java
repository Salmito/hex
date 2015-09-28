package com.salmito.hex.programs.hex.entities;

import android.os.SystemClock;

import com.salmito.hex.engine.Program;
import com.salmito.hex.engine.Thing;
import com.salmito.hex.engine.things.geometry.Point3f;
import com.salmito.hex.programs.hex.HexProgram;

import java.util.concurrent.ConcurrentHashMap;

public class HexMap implements Thing {

    private final int size;
    private final HexProgram program;
    private ConcurrentHashMap<HexCoord, Hexagon> map;

    public HexMap(HexProgram program, int size) {
        this.size = size;
        map = new ConcurrentHashMap<HexCoord, Hexagon>();
        this.program = program;
    }

    public int getSize() {
        return size;
    }

    public boolean hasHexagon(int x, int y) {
        return hasHexagon(new HexCoord(x, y));
    }

    public boolean hasHexagon(HexCoord p) {
        return map.containsKey(p);
    }

    public int getColor(int i, int j) {
        long time = (SystemClock.uptimeMillis() / 250) % 4L;
        return (Math.abs(i) + Math.abs(j) + (int) time) % 5;
    }

    public Hexagon getHexagon(int i, int j) {
        final HexCoord p = new HexCoord(i, j);
        Hexagon hex = map.get(p);
        if (hex == null) {
            hex = new Hexagon(program, p.getQ(), p.getR());
            map.put(p, hex);
        }
        return hex;
    }

    public void drawLine(HexCoord c1,HexCoord c2) {

    }

    public Hexagon getHexagon(HexCoord p) {
        Hexagon hex = map.get(p);
        if (hex == null) {
            hex = new Hexagon(program, p.getQ(), p.getR());
            map.put(p, hex);
        }
        return hex;
    }

    @Override
    public void draw(long time, Program program) {
        Point3f screenTop = this.program.getScreenTop();
        Point3f screenBottom = this.program.getScreenBottom();


        for(Hexagon x:map.values()) {
            x.draw(time, HexProgram.getProgram());
        }
    }

    @Override
    public void clean() {
        map.clear();
    }

}
