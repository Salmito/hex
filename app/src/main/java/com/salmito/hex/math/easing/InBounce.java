package com.salmito.hex.math.easing;

/**
 * Created by tiago on 9/16/2015.
 */
public class InBounce implements EasingFunction {
    private OutBounce ob=new OutBounce();
    @Override
    public float f(float t) {
        return 1 - ob.f(1 - t);
    }
}
