package com.igentuman.kaka.entity.animation;

import com.igentuman.kaka.entity.model.KakaDemonModel;
import net.minecraft.util.Mth;

public class AttackAnimation implements KakaDemonAnimation {
    public static AttackAnimation instance = new AttackAnimation();

    @Override
    public void playAnimation(KakaDemonModel<?> demon, float deltaTime) {
        float normalizedTime = deltaTime / getAnimationDuration();

        // Define animation phases
        float windupPhase = 0.3f;           // First 30% - winding up
        float attackPhase = 0.5f;           // 20% - attack motion
        float recoveryPhase = 1.0f;         // 50% - recovery

        if (normalizedTime < windupPhase) {
            // Phase 1: Wind up - both arms back
            float windupProgress = normalizedTime / windupPhase;
            
            // Both arms wind back
            demon.rightArm.xRot = Mth.lerp(windupProgress, 0f, -0.8f);
            demon.rightArm.zRot = Mth.lerp(windupProgress, 0f, 0.3f);
            demon.leftArm.xRot = Mth.lerp(windupProgress, 0f, -0.8f);
            demon.leftArm.zRot = Mth.lerp(windupProgress, 0f, -0.3f);
            
            // Body leans back slightly
            demon.body.xRot = Mth.lerp(windupProgress, 0f, -0.2f);
            
            // Head tilts back
            demon.head.xRot = Mth.lerp(windupProgress, 0f, -0.3f);
        }
        else if (normalizedTime < attackPhase) {
            // Phase 2: Attack motion - quick forward swing
            float attackProgress = (normalizedTime - windupPhase) / (attackPhase - windupPhase);
            
            // Both arms swing forward
            demon.rightArm.xRot = Mth.lerp(attackProgress, -0.8f, 0.6f);
            demon.rightArm.zRot = Mth.lerp(attackProgress, 0.3f, 0.1f);
            demon.leftArm.xRot = Mth.lerp(attackProgress, -0.8f, 0.6f);
            demon.leftArm.zRot = Mth.lerp(attackProgress, -0.3f, -0.1f);
            
            // Body lunges forward
            demon.body.xRot = Mth.lerp(attackProgress, -0.2f, 0.4f);
            
            // Head follows the attack
            demon.head.xRot = Mth.lerp(attackProgress, -0.3f, 0.5f);
        }
        else {
            // Phase 3: Recovery to neutral
            float recoveryProgress = (normalizedTime - attackPhase) / (recoveryPhase - attackPhase);
            
            // Arms return to neutral
            demon.rightArm.xRot = Mth.lerp(recoveryProgress, 0.6f, 0f);
            demon.rightArm.zRot = Mth.lerp(recoveryProgress, 0.1f, 0f);
            demon.leftArm.xRot = Mth.lerp(recoveryProgress, 0.6f, 0f);
            demon.leftArm.zRot = Mth.lerp(recoveryProgress, -0.1f, 0f);
            
            // Body returns to neutral
            demon.body.xRot = Mth.lerp(recoveryProgress, 0.4f, 0f);
            
            // Head returns to neutral
            demon.head.xRot = Mth.lerp(recoveryProgress, 0.5f, 0f);
        }
    }

    @Override
    public float getAnimationDuration() {
        return 0.8f * 7; // Quick melee attack
    }
}