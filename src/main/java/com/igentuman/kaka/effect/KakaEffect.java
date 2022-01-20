package com.igentuman.kaka.effect;

import com.igentuman.kaka.setup.Registration;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class KakaEffect extends MobEffect {
    public KakaEffect(MobEffectCategory effectCategory, int p_19452_) {
        super(effectCategory, p_19452_);
    }
    public int ticksPassed = 0;

    public void applyInstantenousEffect(@Nullable Entity entity, @Nullable Entity entity1, LivingEntity livingEntity, int p_19465_, double p_19466_)
    {
        int j = (int)(p_19466_ * (double)(6 << p_19465_) + 0.5D);

        if (entity == null) {
            livingEntity.hurt(DamageSource.MAGIC, (float)j);
        } else {
            livingEntity.hurt(DamageSource.indirectMagic(entity, entity1), (float)j);
        }
        this.applyEffectTick(livingEntity, p_19465_);
    }

    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        int k = 50 >> p_19456_;
        if (k > 0) {
            return p_19455_ % k == 0;
        }
        return true;
    }

    public void applyEffectTick(LivingEntity ent, int id)
    {
        if (ent.getHealth() > 1.0F) {
            ent.hurt(DamageSource.MAGIC, 1F);
            ent.addEffect(new MobEffectInstance(MobEffects.POISON,200));
        }

        if (ent instanceof Player) {
            ((Player)ent).causeFoodExhaustion(0.005F * (float)(id + 1));
            ((Player)ent).addEffect(new MobEffectInstance(MobEffects.BLINDNESS,200));
            ((Player)ent).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,200));
            if (!((Player) ent).level.isClientSide) {
                if(ticksPassed > 20) {
                    ((Player) ent).spawnAtLocation(Registration.PLAYER_KAKA.get());
                    ticksPassed = 0;
                }
                ticksPassed++;
            }
        }
    }
}
