package com.salmito.hex.math.easing;

/**
 * Created by tiago on 9/16/2015.
 */
public class InOutQuart implements EasingFunction {
    @Override
    public float easy(float t) {
        return t < .5f ? 8 * t * t * t * t : 1 - 8 * (--t) * t * t * t;
    }
}
