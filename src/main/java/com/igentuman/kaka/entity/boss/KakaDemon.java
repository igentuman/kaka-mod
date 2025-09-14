package com.igentuman.kaka.entity.boss;


import com.igentuman.kaka.entity.ThrowKakaBlock;
import com.igentuman.kaka.entity.goal.KakaBurstGoal;
import com.igentuman.kaka.entity.goal.KakaRangedAttackGoal;
import com.igentuman.kaka.entity.goal.SlamAttackGoal;

import com.igentuman.kaka.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class KakaDemon extends Monster implements PowerableMob, RangedAttackMob {

    // Boss parameters
    public static final float SLAM_ATTACK_RANGE = 8.0F;
    public static final int SLAM_ATTACK_COOLDOWN = 80; // 4 seconds
    public static final int KAKA_BURST_COOLDOWN = 60; // 3 seconds
    public static final float KAKA_BURST_RANGE = 8.0F;

    // Ranged attack parameters
    public static final int RANGED_ATTACK_COOLDOWN = 20;
    public static final float MIN_RANGED_ATTACK_DISTANCE = 16.0F;
    public static final float MAX_RANGED_ATTACK_DISTANCE = 64.0F;

    public int slamAttackCooldownRemaining = 0;
    public int kakaBurstCooldownRemaining = 0;
    public int rangedAttackCooldownRemaining = 0;
    public final Random random = new Random();
    public boolean isExecutingAttack = false;
    
    // Animation tracking
    public byte currentAnimationEvent = 0;
    public int animationEventTime = 0;

    // Boss bar
    private final ServerBossEvent bossEvent = new ServerBossEvent(
            Component.translatable("entity.kaka.kaka_demon"),
            BossEvent.BossBarColor.PURPLE,
            BossEvent.BossBarOverlay.PROGRESS
    );

    private static final Predicate<LivingEntity> LIVING_ENTITY_SELECTOR = (entity) -> {
        return !(entity instanceof KakaDemon) && entity.attackable();
    };

    public KakaDemon(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.setHealth(this.getMaxHealth());
        this.xpReward = 150;
        this.setPersistenceRequired();
    }
    protected void dropCustomDeathLoot(DamageSource p_31464_, int p_31465_, boolean p_31466_) {
        super.dropCustomDeathLoot(p_31464_, p_31465_, p_31466_);
        ItemEntity itementity = this.spawnAtLocation(Registration.KAKA_BLOCK.get());
        if (itementity != null) {
            itementity.setExtendedLifetime();
        }

    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getDirectEntity() instanceof AbstractArrow) {
            pAmount = pAmount * 0.5F; // Reduce arrow damage
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        // Set animation event for model
        this.currentAnimationEvent = 8;
        this.animationEventTime = this.tickCount;
        
        this.level().broadcastEntityEvent(this, (byte) 8);
        boolean attackResult = super.doHurtTarget(pEntity);

        if (attackResult && pEntity instanceof LivingEntity livingEntity) {
            // Apply kaka effect instead of radiation
            livingEntity.addEffect(new MobEffectInstance(Registration.KAKA_EFFECT.get(), 200, 1));
            
            Vec3 knockbackDirection = pEntity.position().subtract(this.position()).normalize().scale(2.0);
            pEntity.setDeltaMovement(pEntity.getDeltaMovement().add(knockbackDirection));

            livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
        }

        return attackResult;
    }

    public void checkDespawn() {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.discard();
        } else {
            this.noActionTime = 0;
        }
    }
    @Override
    public void performRangedAttack(LivingEntity livingEntity, float p_33318_) {
        this.executeRangedAttack();
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, 12.0)
                .add(Attributes.MAX_HEALTH, 500.0D)
                .add(Attributes.FOLLOW_RANGE, 70)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
    }

    @Override
    public void tick() {
        super.tick();

        // Update boss bar
        bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

        // Decrease cooldowns
        if (slamAttackCooldownRemaining > 0) {
            slamAttackCooldownRemaining--;
        }

        if (kakaBurstCooldownRemaining > 0) {
            kakaBurstCooldownRemaining--;
        }

        if (rangedAttackCooldownRemaining > 0) {
            rangedAttackCooldownRemaining--;
        }

        // Add ambient particles
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.ITEM_SLIME, 
                this.getX() + this.random.nextGaussian() * 0.3F, 
                this.getY() + this.random.nextFloat() * 3.3F, 
                this.getZ() + this.random.nextGaussian() * 0.3F, 
                0.0D, 0.0D, 0.0D);
        }
    }

    /**
     * Execute a kaka burst attack that affects players in an area
     */
    public void executeKakaBurst() {
        if (kakaBurstCooldownRemaining <= 0 && !isExecutingAttack) {
            isExecutingAttack = true;
            
            // Set animation event for model
            this.currentAnimationEvent = 5;
            this.animationEventTime = this.tickCount;
            
            // Broadcast animation event
            this.level().broadcastEntityEvent(this, (byte) 5);

            this.playSound(Registration.FART.get(), 0.7F, 1F);

            AABB kakaBox = this.getBoundingBox().inflate(KAKA_BURST_RANGE);
            List<Player> nearbyPlayers = this.level().getEntitiesOfClass(Player.class, kakaBox);

            if (this.level() instanceof ServerLevel serverLevel) {
                double entityY = this.getY() + 0.5;
                Vec3 center = this.position();

                for (int i = 0; i < 50; i++) {
                    double angle = random.nextDouble() * Math.PI * 2;
                    double radius = random.nextDouble() * KAKA_BURST_RANGE;
                    double height = random.nextDouble() * 3;

                    double xPos = center.x + radius * Math.cos(angle);
                    double yPos = entityY + height + 1;
                    double zPos = center.z + radius * Math.sin(angle);

                    double xVel = (xPos - center.x) * 0.05;
                    double yVel = random.nextDouble() * 0.05;
                    double zVel = (zPos - center.z) * 0.05;

                    serverLevel.sendParticles(
                            ParticleTypes.ITEM_SLIME,
                            xPos, yPos, zPos,
                            1,
                            xVel, yVel, zVel,
                            0.02
                    );
                }

                for (int ring = 0; ring < 3; ring++) {
                    double ringRadius = (ring + 1) * (KAKA_BURST_RANGE / 3);
                    int particlesInRing = 20 + (ring * 10);

                    for (int i = 0; i < particlesInRing; i++) {
                        double angle = (Math.PI * 2 * i) / particlesInRing;
                        double xPos = center.x + ringRadius * Math.cos(angle);
                        double yPos = center.y + 0.2;
                        double zPos = center.z + ringRadius * Math.sin(angle);

                        serverLevel.sendParticles(
                                ParticleTypes.ITEM_SLIME,
                                xPos, yPos, zPos,
                                1,
                                0, 0.05, 0,
                                0.01
                        );
                    }
                }
            }

            for (Player player : nearbyPlayers) {
                player.addEffect(new MobEffectInstance(Registration.KAKA_EFFECT.get(), 200, 1));
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20, 0));
                player.addEffect(new MobEffectInstance(MobEffects.POISON, 10, 0));
            }

            kakaBurstCooldownRemaining = KAKA_BURST_COOLDOWN;
            isExecutingAttack = false;
        }
    }

    /**
     * Execute a ground slam attack that affects players in an area
     */
    public void executeSlamAttack() {
        if (slamAttackCooldownRemaining <= 0 && !isExecutingAttack) {
            isExecutingAttack = true;
            
            // Set animation event for model
            this.currentAnimationEvent = 4;
            this.animationEventTime = this.tickCount;
            
            // Broadcast animation event
            this.level().broadcastEntityEvent(this, (byte) 4);

            this.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 0.1F);

            double entityX = this.getX();
            double entityY = this.getY();
            double entityZ = this.getZ();
            ServerLevel serverLevel = (ServerLevel) this.level();
            RandomSource random = this.getRandom();

            for (int i = 0; i < 40; i++) {
                double angle = random.nextDouble() * Math.PI * 2;
                double distance = random.nextDouble() * SLAM_ATTACK_RANGE;
                double xPos = entityX + Math.cos(angle) * distance;
                double yPos = entityY + 0.1;
                double zPos = entityZ + Math.sin(angle) * distance;

                double xVel = Math.cos(angle) * 0.15;
                double zVel = Math.sin(angle) * 0.15;

                if (i % 3 == 0) {
                    serverLevel.sendParticles(
                            ParticleTypes.LARGE_SMOKE,
                            xPos, yPos, zPos,
                            1, // count
                            xVel * 0.2, 0.05, zVel * 0.2,
                            0.01 // speed
                    );
                } else {
                    serverLevel.sendParticles(
                            ParticleTypes.CAMPFIRE_COSY_SMOKE,
                            xPos, yPos, zPos,
                            1, // count
                            xVel, 0.1, zVel,
                            0.05 // speed
                    );
                }
            }

            AABB affectedArea = this.getBoundingBox().inflate(SLAM_ATTACK_RANGE);
            List<LivingEntity> nearbyEntities = this.level().getEntitiesOfClass(
                    LivingEntity.class,
                    affectedArea,
                    entity -> entity != this
            );

            for (LivingEntity entity : nearbyEntities) {
                double distance = entity.distanceTo(this);
                float damageAmount = 10.0f * (1.0f - (float)(distance / SLAM_ATTACK_RANGE));

                entity.hurt(this.damageSources().mobAttack(this), damageAmount);

                Vec3 knockbackDirection = entity.position().subtract(this.position()).normalize();
                double distanceMult = Math.log(SLAM_ATTACK_RANGE/distance)+0.6;
                entity.setDeltaMovement(
                    knockbackDirection.x * distanceMult,
                    1 * distanceMult,
                    knockbackDirection.z * distanceMult
                );
            }

            isExecutingAttack = false;
            slamAttackCooldownRemaining = SLAM_ATTACK_COOLDOWN;
        }
    }

    /**
     * Execute a ranged attack by throwing kaka blocks at the target
     */
    public void executeRangedAttack() {
        if (rangedAttackCooldownRemaining <= 0 && !isExecutingAttack && !this.level().isClientSide()) {
            isExecutingAttack = true;
            
            // Set animation event for model
            this.currentAnimationEvent = 7;
            this.animationEventTime = this.tickCount;
            
            // Broadcast animation event
            this.level().broadcastEntityEvent(this, (byte) 7);
            
            this.playSound(SoundEvents.WITHER_SHOOT, 1.0F, 0.2F);
            LivingEntity target = this.getTarget();
            if (target != null && target.isAlive()) {
                Vec3 eyePos = new Vec3(this.getX(), this.getEyeY(), this.getZ());
                Vec3 targetPos = new Vec3(target.getX(), target.getEyeY(), target.getZ());
                Vec3 direction = targetPos.subtract(eyePos).normalize();

                Vec3 targetVelocity = target.getDeltaMovement();
                double distance = target.distanceTo(this);
                double velocityScale = 3;
                double timeToHit = distance / velocityScale;
                Vec3 predictedPos = targetPos.add(targetVelocity.x * timeToHit, 0, targetVelocity.z * timeToHit);
                Vec3 adjustedDirection = predictedPos.subtract(eyePos).normalize();
                double inaccuracy = 0.1;
                double verticalAdjustment = Math.log10(distance) * 0.05;

                Vec3 rightVector = new Vec3(direction.z, 0, -direction.x).normalize().scale(0.750);
                Vec3 leftVector = rightVector.scale(-0.750);

                ThrowKakaBlock leftProjectile = new ThrowKakaBlock(this.level(), this);
                leftProjectile.setPos(
                        this.getX() + leftVector.x * 1.0 + direction.x * 0.5,
                        this.getEyeY() - 0.3,
                        this.getZ() + leftVector.z * 1.0 + direction.z * 0.5
                );

                leftProjectile.shoot(
                        adjustedDirection.x,
                        adjustedDirection.y + verticalAdjustment,
                        adjustedDirection.z,
                        (float) velocityScale,
                        (float) inaccuracy
                );

                this.level().addFreshEntity(leftProjectile);

                ThrowKakaBlock rightProjectile = new ThrowKakaBlock(this.level(), this);
                rightProjectile.setPos(
                        this.getX() + rightVector.x * 1.0 + direction.x * 0.5,
                        this.getEyeY() - 0.3,
                        this.getZ() + rightVector.z * 1.0 + direction.z * 0.5
                );

                rightProjectile.shoot(
                        adjustedDirection.x,
                        adjustedDirection.y + verticalAdjustment,
                        adjustedDirection.z,
                        (float) velocityScale,
                        (float) inaccuracy
                );

                this.level().addFreshEntity(rightProjectile);
            }

            rangedAttackCooldownRemaining = RANGED_ATTACK_COOLDOWN;
            isExecutingAttack = false;
        }
    }
    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        bossEvent.removePlayer(player);
    }

    @Override
    public boolean causeFallDamage(float p_149589_, float p_149590_, DamageSource p_149591_) {
        return false;
    }

    @Override
    public boolean addEffect(MobEffectInstance p_182397_, @Nullable Entity p_182398_) {
        return false;
    }

    @Override
    public boolean isPowered() {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag data) {
        super.addAdditionalSaveData(data);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag data) {
        super.readAdditionalSaveData(data);
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(@Nullable Component p_31476_) {
        super.setCustomName(p_31476_);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.WITHER) || super.isInvulnerableTo(source);
    }
    protected PathNavigation createNavigation(Level level) {
        GroundPathNavigation pathnavigation = new GroundPathNavigation(this, level);
        pathnavigation.setCanOpenDoors(false);
        pathnavigation.setCanFloat(true);
        pathnavigation.setCanPassDoors(true);
        return pathnavigation;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SlamAttackGoal(this));
        this.goalSelector.addGoal(2, new KakaBurstGoal(this));
        this.goalSelector.addGoal(3, new KakaRangedAttackGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(9, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 64.0F));
        this.goalSelector.addGoal(20, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return Registration.KAKADEMON_ATTACK.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_31500_) {
        return Registration.KAKADEMON_HIT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return Registration.KAKADEMON_DIE.get();
    }

    public int getAttackAnimationTick() {
        return this.tickCount; // Simple implementation for animation timing
    }

    public void makeInvulnerable() {
        // Simple implementation - just set the boss to full health
        this.setHealth(this.getMaxHealth());
        this.bossEvent.setProgress(1.0F);
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == 4 || pId == 5 || pId == 6 || pId == 7 || pId == 8) {
            // Handle animation events for client-side rendering
            if (this.level().isClientSide) {
                this.currentAnimationEvent = pId;
                this.animationEventTime = this.tickCount;
            }
        } else {
            super.handleEntityEvent(pId);
        }
    }
}
