package com.igentuman.kaka.entity;

import com.igentuman.kaka.setup.Registration;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ThrowKakaBlock extends ThrowableItemProjectile {

    public ThrowKakaBlock(EntityType<? extends ThrowKakaBlock> entityType, Level level) {
        super(entityType, level);
    }

    public ThrowKakaBlock(Level level, LivingEntity shooter) {
        super(Registration.THROW_KAKA_BLOCK.get(), shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        return Registration.KAKA_BLOCK_ITEM.get();
    }

    @Override
    public void tick() {
        super.tick();
        
        // Add particle trail
        if (this.level().isClientSide) {
            Vec3 motion = this.getDeltaMovement();
            double x = this.getX() - motion.x * 0.25D;
            double y = this.getY() - motion.y * 0.25D;
            double z = this.getZ() - motion.z * 0.25D;
            
            this.level().addParticle(ParticleTypes.ITEM_SLIME, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        
        if (entity instanceof LivingEntity livingEntity) {
            float damage = 8.0F;
            DamageSource damageSource = this.damageSources().thrown(this, this.getOwner());
            livingEntity.hurt(damageSource, damage);
            
            // Knockback effect
            Vec3 knockback = this.getDeltaMovement().normalize().scale(1.5);
            livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().add(knockback.x, 0.3, knockback.z));
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        
        if (!this.level().isClientSide) {
            // Create explosion particles
            for (int i = 0; i < 8; i++) {
                double xOffset = (this.random.nextDouble() - 0.5) * 2.0;
                double yOffset = this.random.nextDouble() * 2.0;
                double zOffset = (this.random.nextDouble() - 0.5) * 2.0;
                
                this.level().addParticle(ParticleTypes.ITEM_SLIME, 
                    this.getX() + xOffset, 
                    this.getY() + yOffset, 
                    this.getZ() + zOffset, 
                    0.0, 0.0, 0.0);
            }
            
            this.playSound(SoundEvents.SLIME_BLOCK_BREAK, 1.0F, 1.0F);
            this.discard();
        }
    }
}
