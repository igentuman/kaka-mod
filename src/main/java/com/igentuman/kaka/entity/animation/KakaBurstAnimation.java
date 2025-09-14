package com.igentuman.kaka.entity.animation;

import com.igentuman.kaka.entity.model.KakaDemonModel;
import net.minecraft.util.Mth;

public class KakaBurstAnimation implements KakaDemonAnimation {
    public static KakaBurstAnimation instance = new KakaBurstAnimation();

    @Override
    public void playAnimation(KakaDemonModel<?> demon, float deltaTime) {
        float normalizedTime = deltaTime / getAnimationDuration();

        // Define animation phases
        float chargePhase = 0.4f;           // First 40% - charging up
        float burstPhase = 0.6f;            // 20% - burst release
        float recoveryPhase = 1.0f;         // 40% - recovery

        if (normalizedTime < chargePhase) {
            // Phase 1: Charging up - arms spread wide, body leans back
            float chargeProgress = normalizedTime / chargePhase;
            
            // Arms spread wide and slightly back
            demon.rightArm.xRot = Mth.lerp(chargeProgress, 0f, -0.5f);
            demon.rightArm.zRot = Mth.lerp(chargeProgress, 0f, 0.8f);
            demon.leftArm.xRot = Mth.lerp(chargeProgress, 0f, -0.5f);
            demon.leftArm.zRot = Mth.lerp(chargeProgress, 0f, -0.8f);
            
            // Body leans back slightly
            demon.body.xRot = Mth.lerp(chargeProgress, 0f, -0.3f);
            
            // Head tilts back
            demon.head.xRot = Mth.lerp(chargeProgress, 0f, -0.4f);
            
            // Subtle pulsing effect during charge
            float pulse = Mth.sin(chargeProgress * 12.0f) * 0.05f;
            demon.body.yScale = 1.0f + pulse;
            demon.body.xScale = 1.0f + pulse;
            demon.body.zScale = 1.0f + pulse;
        }
        else if (normalizedTime < burstPhase) {
            // Phase 2: Burst release - quick forward motion
            float burstProgress = (normalizedTime - chargePhase) / (burstPhase - chargePhase);
            
            // Arms thrust forward
            float armRotation = Mth.lerp(burstProgress, -0.5f, 0.8f);
            demon.rightArm.xRot = armRotation;
            demon.leftArm.xRot = armRotation;
            demon.rightArm.zRot = Mth.lerp(burstProgress, 0.8f, 0.2f);
            demon.leftArm.zRot = Mth.lerp(burstProgress, -0.8f, -0.2f);
            
            // Body thrusts forward
            demon.body.xRot = Mth.lerp(burstProgress, -0.3f, 0.5f);
            
            // Head snaps forward
            demon.head.xRot = Mth.lerp(burstProgress, -0.4f, 0.6f);
            
            // Expansion effect during burst
            float expansion = Mth.lerp(burstProgress, 0.05f, 0.15f);
            demon.body.yScale = 1.0f + expansion;
            demon.body.xScale = 1.0f + expansion;
            demon.body.zScale = 1.0f + expansion;
        }
        else {
            // Phase 3: Recovery to neutral
            float recoveryProgress = (normalizedTime - burstPhase) / (recoveryPhase - burstPhase);
            
            // Arms return to neutral
            demon.rightArm.xRot = Mth.lerp(recoveryProgress, 0.8f, 0f);
            demon.rightArm.zRot = Mth.lerp(recoveryProgress, 0.2f, 0f);
            demon.leftArm.xRot = Mth.lerp(recoveryProgress, 0.8f, 0f);
            demon.leftArm.zRot = Mth.lerp(recoveryProgress, -0.2f, 0f);
            
            // Body returns to neutral
            demon.body.xRot = Mth.lerp(recoveryProgress, 0.5f, 0f);
            
            // Head returns to neutral
            demon.head.xRot = Mth.lerp(recoveryProgress, 0.6f, 0f);
            
            // Scale returns to normal
            float scale = Mth.lerp(recoveryProgress, 0.15f, 0f);
            demon.body.yScale = 1.0f + scale;
            demon.body.xScale = 1.0f + scale;
            demon.body.zScale = 1.0f + scale;
        }
    }

    @Override
    public float getAnimationDuration() {
        return 1.2f * 7; // Slightly shorter than slam attack
    }
}