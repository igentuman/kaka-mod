package com.igentuman.kaka.entity.animation;

import com.igentuman.kaka.entity.model.KakaDemonModel;
import net.minecraft.util.Mth;

public class RangedAttackAnimation implements KakaDemonAnimation {
    public static RangedAttackAnimation instance = new RangedAttackAnimation();

    @Override
    public void playAnimation(KakaDemonModel<?> demon, float deltaTime) {
        float normalizedTime = deltaTime / getAnimationDuration();

        // Define animation phases
        float windupPhase = 0.4f;           // First 40% - winding up
        float throwPhase = 0.6f;            // 20% - throwing motion
        float recoveryPhase = 1.0f;         // 40% - recovery

        if (normalizedTime < windupPhase) {
            // Phase 1: Wind up - right arm back, body turns slightly
            float windupProgress = normalizedTime / windupPhase;
            
            // Right arm winds back
            demon.rightArm.xRot = Mth.lerp(windupProgress, 0f, -1.2f);
            demon.rightArm.zRot = Mth.lerp(windupProgress, 0f, 0.5f);
            demon.rightArm.yRot = Mth.lerp(windupProgress, 0f, -0.3f);
            
            // Left arm stays more neutral but slightly forward for balance
            demon.leftArm.xRot = Mth.lerp(windupProgress, 0f, 0.2f);
            demon.leftArm.zRot = Mth.lerp(windupProgress, 0f, -0.2f);
            
            // Body turns slightly to the right for windup
            demon.body.yRot = Mth.lerp(windupProgress, 0f, 0.3f);
            demon.body.xRot = Mth.lerp(windupProgress, 0f, -0.1f);
            
            // Head follows the body turn
            demon.head.yRot = Mth.lerp(windupProgress, 0f, 0.2f);
        }
        else if (normalizedTime < throwPhase) {
            // Phase 2: Throwing motion - quick forward swing
            float throwProgress = (normalizedTime - windupPhase) / (throwPhase - windupPhase);
            
            // Right arm swings forward rapidly
            demon.rightArm.xRot = Mth.lerp(throwProgress, -1.2f, 1.0f);
            demon.rightArm.zRot = Mth.lerp(throwProgress, 0.5f, -0.2f);
            demon.rightArm.yRot = Mth.lerp(throwProgress, -0.3f, 0.2f);
            
            // Left arm swings back for balance
            demon.leftArm.xRot = Mth.lerp(throwProgress, 0.2f, -0.5f);
            demon.leftArm.zRot = Mth.lerp(throwProgress, -0.2f, -0.5f);
            
            // Body follows through with the throw
            demon.body.yRot = Mth.lerp(throwProgress, 0.3f, -0.2f);
            demon.body.xRot = Mth.lerp(throwProgress, -0.1f, 0.3f);
            
            // Head follows the throw
            demon.head.yRot = Mth.lerp(throwProgress, 0.2f, -0.1f);
            demon.head.xRot = Mth.lerp(throwProgress, 0f, 0.2f);
        }
        else {
            // Phase 3: Recovery to neutral
            float recoveryProgress = (normalizedTime - throwPhase) / (recoveryPhase - throwPhase);
            
            // Right arm returns to neutral
            demon.rightArm.xRot = Mth.lerp(recoveryProgress, 1.0f, 0f);
            demon.rightArm.zRot = Mth.lerp(recoveryProgress, -0.2f, 0f);
            demon.rightArm.yRot = Mth.lerp(recoveryProgress, 0.2f, 0f);
            
            // Left arm returns to neutral
            demon.leftArm.xRot = Mth.lerp(recoveryProgress, -0.5f, 0f);
            demon.leftArm.zRot = Mth.lerp(recoveryProgress, -0.5f, 0f);
            
            // Body returns to neutral
            demon.body.yRot = Mth.lerp(recoveryProgress, -0.2f, 0f);
            demon.body.xRot = Mth.lerp(recoveryProgress, 0.3f, 0f);
            
            // Head returns to neutral
            demon.head.yRot = Mth.lerp(recoveryProgress, -0.1f, 0f);
            demon.head.xRot = Mth.lerp(recoveryProgress, 0.2f, 0f);
        }
    }

    @Override
    public float getAnimationDuration() {
        return 1.0f * 7; // Quick throwing animation
    }
}