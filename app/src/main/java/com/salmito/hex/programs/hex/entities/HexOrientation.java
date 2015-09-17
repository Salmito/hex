package com.salmito.hex.programs.hex.entities;

import static java.lang.Math.sqrt;

/**
 * Created by Tiago on 17/09/2015.
 */
public enum HexOrientation {
    POINTY((float) sqrt(3.0), (float) sqrt(3.0) / 2.0f, 0.0f, 3.0f / 2.0f,
            (float) sqrt(3.0) / 3.0f, -1.0f / 3.0f, 0.0f, 2.0f / 3.0f,
            0.5f),
    FLAT(3f / 2f, 0f, (float) sqrt(3.0) / 2f, (float) sqrt(3.0),
            2f / 3f, 0f, -1f / 3.0f, (float) sqrt(3.0) / 3f,
            0f);

    public final float f0, f1, f2, f3;
    public final float b0, b1, b2, b3;
    public final float start_angle; // in multiples of 60°

    HexOrientation(float f0_, float f1_, float f2_, float f3_,
                   float b0_, float b1_, float b2_, float b3_,
                   float start_angle_) {
        f0 = f0_;
        f1 = f1_;
        f2 = f2_;
        f3 = f3_;
        b0 = b0_;
        b1 = b1_;
        b2 = b2_;
        b3 = b3_;
        start_angle = start_angle_;
    }

}
