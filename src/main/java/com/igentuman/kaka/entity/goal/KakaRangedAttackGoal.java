package com.igentuman.kaka.entity.goal;

import com.igentuman.kaka.entity.boss.KakaDemon;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class KakaRangedAttackGoal extends Goal {
    private final KakaDemon kakaDemon;
    private LivingEntity target;
    private int attackCooldown = 0;

    public KakaRangedAttackGoal(KakaDemon kakaDemon) {
        this.kakaDemon = kakaDemon;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        this.target = this.kakaDemon.getTarget();
        if (this.target == null || !this.target.isAlive()) {
            return false;
        }

        double distance = this.kakaDemon.distanceTo(this.target);
        return distance >= KakaDemon.MIN_RANGED_ATTACK_DISTANCE && 
               distance <= KakaDemon.MAX_RANGED_ATTACK_DISTANCE &&
               this.kakaDemon.rangedAttackCooldownRemaining <= 0 && 
               !this.kakaDemon.isExecutingAttack;
    }

    @Override
    public boolean canContinueToUse() {
        return this.target != null && this.target.isAlive() && this.attackCooldown > 0;
    }

    @Override
    public void start() {
        this.attackCooldown = 25; // 1.25 second animation time
        this.kakaDemon.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.target != null) {
            this.kakaDemon.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            
            if (this.attackCooldown == 12) { // Execute attack halfway through animation
                this.kakaDemon.executeRangedAttack();
            }
            
            this.attackCooldown--;
        }
    }

    @Override
    public void stop() {
        this.target = null;
        this.attackCooldown = 0;
    }
}