package com.salmito.hex.math.easing;

/**
 * Created by tiago on 9/16/2015.
 */
public class InOutElastic implements EasingFunction {
    @Override
    public float easy(float n) {
        if (n == 0) return 0;
        if ((n *= 2) == 2) return 1;
        float p = 0.45f;	// 0.3 * 1.5
        float s = 0.1125f;	// p / (2 * Math.PI) * Math.asin(1)
        if (n < 1) return -.5f * (float)(Math.pow(2, 10 * (n -= 1)) * Math.sin((n * 1 - s) * (2 * Math.PI) / p));
        return (float)Math.pow(2, -10 * (n -= 1)) * (float)Math.sin((n * 1 - s) * (2 * Math.PI) / p ) * .5f + 1;
    }
}
