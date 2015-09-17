package com.salmito.hex.math.easing;

/**
 * Created by tiago on 9/16/2015.
 */
public class InBounce implements EasingFunction {
    private OutBounce ob=new OutBounce();
    @Override
    public float easy(float t) {
        return 1 - ob.easy(1 - t);
    }
}
