package com.salmito.hex.math.easing;

/**
 * Created by tiago on 9/16/2015.
 */
public class InBack implements EasingFunction {
    @Override
    public float f(float n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        float s = 1.70158f;
        return n * n * ((s + 1) * n - s);
    }
}
