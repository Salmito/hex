package com.salmito.hex.math.easing;

/**
 * Created by tiago on 9/16/2015.
 */
public class InElastic implements EasingFunction {
    @Override
    public float f(float n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        float p = 0.3f;
        float s = 0.075f;	// p / (2 * Math.PI) * Math.asin(1)
        return (float)-(Math.pow(2., 10. * (n -= 1)) * Math.sin((n - s) * (2. * Math.PI) / p));
    }
}
