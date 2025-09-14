package com.igentuman.kaka.entity.animation;

import com.igentuman.kaka.entity.model.KakaDemonModel;

public interface KakaDemonAnimation {
    void playAnimation(KakaDemonModel<?> demon, float deltaTime);
    float getAnimationDuration();
}