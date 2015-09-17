package com.salmito.hex.math.easing;

/**
 * Created by tiago on 9/16/2015.
 */
public class OutBounce implements EasingFunction {
    @Override
    public float easy(float t) {
        if (t == 0) return 0f;
        if (t == 1) return 1f;
        if (t < (1f / 2.75f)) {
            return (7.5625f * t * t);
        } else if (t < (2f / 2.75f)) {
            return (7.5625f * (t -= (1.5f / 2.75f)) * t + .75f);
        } else if (t < (2.5f / 2.75f)) {
            return (7.5625f * (t -= (2.25f / 2.75f)) * t + .9375f);
        } else {
            return (7.5625f * (t -= (2.625f / 2.75f)) * t + .984375f);
        }

    }
}
