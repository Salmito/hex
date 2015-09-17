package com.salmito.hex.math.easing;

/**
 * Created by tiago on 9/16/2015.
 */
public class InOutQuad implements EasingFunction {
    @Override
    public float easy(float t) {
        return t < .5f ? 2 * t * t : -1 + (4 - 2 * t) * t;
    }
}
