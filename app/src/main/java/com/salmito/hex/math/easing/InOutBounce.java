package com.salmito.hex.math.easing;

/**
 * Created by tiago on 9/16/2015.
 */
public class InOutBounce implements EasingFunction {
    private OutBounce ob=new OutBounce();
    private InBounce ib=new InBounce();
    @Override
    public float f(float n) {
        if (n < 0.5) return ib.f(n * 2f) * .5f;
        return ob.f((n * 2) - 1) * .5f + .5f;
    }
}
