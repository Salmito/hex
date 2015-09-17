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
public class Point2f extends Point3f {
    private static final DecimalFormat format = new DecimalFormat("#.####", new DecimalFormatSymbols(Locale.US)); //NOI18N

    public Point2f(float x, float y) {
        super(x, y, 0f);
    }
    @Override
    public String toString() {
        return String.format(this.getClass().getSimpleName()+"[x=%s, y=%s]", format.format(getX()), format.format(getY()));
    }
}
