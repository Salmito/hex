package com.salmito.hex.math.easing;

/**
 * Created by tiago on 9/16/2015.
 */
public class InOutBack implements EasingFunction {
    @Override
    public float f(float n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        float s = 1.70158f;
        if ((n *= 2) < 1) return 0.5f * (n * n * (((s *= 1.525f) + 1) * n - s));
        return 0.5f * ((n -= 2) * n * (((s *= 1.525f) + 1) * n + s) + 2);
    }
}
