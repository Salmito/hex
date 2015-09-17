package com.salmito.hex.math.easing;

/**
 * Created by tiago on 9/16/2015.
 */
public class InOutBounce implements EasingFunction {
    private OutBounce ob=new OutBounce();
    private InBounce ib=new InBounce();
    @Override
    public float easy(float n) {
        if (n < 0.5) return ib.easy(n * 2f) * .5f;
        return ob.easy((n * 2) - 1) * .5f + .5f;
    }
}
