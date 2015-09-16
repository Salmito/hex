package com.salmito.hex.math.easing;

/**
 * Created by tiago on 9/16/2015.
 */
public class OutQuart implements EasingFunction {
    @Override
    public float f(float t) {
        return 1 - (--t) * t * t * t;
    }
}
