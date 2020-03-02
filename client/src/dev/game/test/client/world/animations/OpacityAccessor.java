package dev.game.test.client.world.animations;

import aurelienribon.tweenengine.TweenAccessor;

import java.util.concurrent.atomic.AtomicReference;

public class OpacityAccessor implements TweenAccessor<AtomicReference<Float>> {

    public static final int ALPHA = 1;

    @Override
    public int getValues(AtomicReference<Float> target, int tweenType, float[] returnValues) {
        if (tweenType == ALPHA) {
            returnValues[0] = target.get();
            return 1;
        }

        return -1;
    }

    @Override
    public void setValues(AtomicReference<Float> target, int tweenType, float[] newValues) {
        if (tweenType == ALPHA) {
            target.set(newValues[0]);
        }
    }
}
