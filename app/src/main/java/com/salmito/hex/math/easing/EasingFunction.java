package com.salmito.hex.math.easing;

/**
 * Created by tiago on 9/16/2015.
 */
public interface EasingFunction {
    public float easy(float t);

    public final static EasingFunction[] easings = {
            new Linear(),
            new InQuad(),
            new OutQuad(),
            new InOutQuad(),
            new InCubic(),
            new OutCubic(),
            new InOutCubic(),
            new InQuad(),
            new OutQuad(),
            new InOutQuad(),
            new InQuint(),
            new OutQuint(),
            new InOutQuint(),
            new InElastic(),
            new OutElastic(),
            new InOutElastic(),
            new InBack(),
            new OutBack(),
            new InOutBack(),
            new InBounce(),
            new OutBounce(),
            new InOutBounce(),

    };
}
