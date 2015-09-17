package com.salmito.hex.math.easing;

import android.util.FloatMath;

/**
 * Created by tiago on 9/16/2015.
 */
public class OutElastic implements EasingFunction {
    @Override
    public float easy(float n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        float p = 0.3f;
        float s = 0.075f;	// p / (2 * Math.PI) * Math.asin(1)
        return (float)Math.pow(2.,-10. * (double)n) * (float)Math.sin((n - s) * (2. * Math.PI) / p) + 1f;
    }
}
