package com.igentuman.kaka.entity.goal;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class RangedAttackGoal extends Goal {
   private final Mob mob;
   private final RangedAttackMob rangedAttackMob;
   @Nullable
   private LivingEntity target;
   private int attackTime = -1;
   private final double speedModifier;
   private int seeTime;
   private final int attackIntervalMin;
   private final int attackIntervalMax;
   private final float attackRadius;
   private final float attackRadiusSqr;

   public RangedAttackGoal(RangedAttackMob mob, double p_25769_, int p_25770_, float p_25771_) {
      this(mob, p_25769_, p_25770_, p_25770_, p_25771_);
   }

   public RangedAttackGoal(RangedAttackMob mob, double p_25774_, int p_25775_, int p_25776_, float p_25777_) {
      if (!(mob instanceof LivingEntity)) {
         throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
      } else {
         this.rangedAttackMob = mob;
         this.mob = (Mob)mob;
         this.speedModifier = p_25774_;
         this.attackIntervalMin = p_25775_;
         this.attackIntervalMax = p_25776_;
         this.attackRadius = p_25777_;
         this.attackRadiusSqr = p_25777_ * p_25777_;
         this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
      }
   }

   public boolean canUse() {
      LivingEntity livingentity = this.mob.getTarget();
      if (livingentity != null && livingentity.isAlive()) {
         this.target = livingentity;
         return true;
      } else {
         return false;
      }
   }

   public boolean canContinueToUse() {
      return this.canUse() || !this.mob.getNavigation().isDone();
   }

   public void stop() {
      this.target = null;
      this.seeTime = 0;
      this.attackTime = -1;
   }

   public boolean requiresUpdateEveryTick() {
      return true;
   }

   public void tick() {
      double d0 = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
      boolean flag = this.mob.getSensing().hasLineOfSight(this.target);
      if (flag) {
         ++this.seeTime;
      } else {
         this.seeTime = 0;
      }

      if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 5) {
         this.mob.getNavigation().stop();
      } else {
         this.mob.getNavigation().moveTo(this.target, this.speedModifier);
      }

      this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
      if (--this.attackTime == 0) {
         if (!flag) {
            return;
         }

         float f = (float)Math.sqrt(d0) / this.attackRadius;
         float f1 = Mth.clamp(f, 0.1F, 1.0F);
         this.rangedAttackMob.performRangedAttack(this.target, f1);
         this.attackTime = Mth.floor(f * (float)(this.attackIntervalMax - this.attackIntervalMin) + (float)this.attackIntervalMin);
         this.mob.swing(InteractionHand.MAIN_HAND);
      } else if (this.attackTime < 0) {
         this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(d0) / (double)this.attackRadius, (double)this.attackIntervalMin, (double)this.attackIntervalMax));
      }

   }
}