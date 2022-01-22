package com.igentuman.kaka.entity.boss;

import com.google.common.collect.ImmutableList;
import com.igentuman.kaka.config.CommonConfig;
import com.igentuman.kaka.entity.boss.attack.KakaThrow;
import com.igentuman.kaka.setup.Registration;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class KakaDemon extends Monster implements PowerableMob, RangedAttackMob {

    private static final EntityDataAccessor<Integer> DATA_TARGET_A = SynchedEntityData.defineId(KakaDemon.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_TARGET_B = SynchedEntityData.defineId(KakaDemon.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_TARGET_C = SynchedEntityData.defineId(KakaDemon.class, EntityDataSerializers.INT);
    private static final List<EntityDataAccessor<Integer>> DATA_TARGETS = ImmutableList.of(DATA_TARGET_A, DATA_TARGET_B, DATA_TARGET_C);
    private static final EntityDataAccessor<Integer> DATA_ID_INV = SynchedEntityData.defineId(KakaDemon.class, EntityDataSerializers.INT);
    private static final int INVULNERABLE_TICKS = 220;
    private int destroyBlocksTick;
    private int attackAnimationTick;

    private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
    private static final Predicate<LivingEntity> LIVING_ENTITY_SELECTOR = (p_31504_) -> {
        return p_31504_.getMobType() != MobType.UNDEAD && p_31504_.attackable();
    };
    private static final TargetingConditions TARGETING_CONDITIONS = TargetingConditions.forCombat().range(20.0D).selector(LIVING_ENTITY_SELECTOR);
    private int nextAttackUpdate = 0;

    public KakaDemon(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.setHealth(this.getMaxHealth());
        this.xpReward = 50;
       // this.swingTime = 2;
    }
    protected void dropCustomDeathLoot(DamageSource p_31464_, int p_31465_, boolean p_31466_) {
        super.dropCustomDeathLoot(p_31464_, p_31465_, p_31466_);
        ItemEntity itementity = this.spawnAtLocation(Registration.KAKA_BLOCK.get());
        if (itementity != null) {
            itementity.setExtendedLifetime();
        }

    }

    public boolean hurt(DamageSource p_31461_, float p_31462_) {
        if (this.isInvulnerableTo(p_31461_)) {
            return false;
        } else if (p_31461_ != DamageSource.DROWN && !(p_31461_.getEntity() instanceof KakaDemon)) {
            if (this.getInvulnerableTicks() > 0 && p_31461_ != DamageSource.OUT_OF_WORLD) {
                return false;
            } else {
                if (this.isPowered()) {
                    Entity entity = p_31461_.getDirectEntity();
                    if (entity instanceof AbstractArrow) {
                        return false;
                    }
                }

                Entity entity1 = p_31461_.getEntity();
                if (entity1 != null && !(entity1 instanceof Player) && entity1 instanceof LivingEntity && ((LivingEntity)entity1).getMobType() == this.getMobType()) {
                    return false;
                } else {
                    if (this.destroyBlocksTick <= 0) {
                        this.destroyBlocksTick = 20;
                    }
                    return super.hurt(p_31461_, p_31462_);
                }
            }
        } else {
            return false;
        }
    }

    public void checkDespawn() {
        if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.discard();
        } else {
            this.noActionTime = 0;
        }
    }
    @Override
    public void performRangedAttack(LivingEntity livingEntity, float p_33318_) {
        this.performRangedAttack();
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.MAX_HEALTH, 500.0D)
                .add(Attributes.FOLLOW_RANGE, 60)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.ARMOR, 6.0D);
    }

    public int getAlternativeTarget(int p_31513_) {
        return this.entityData.get(DATA_TARGETS.get(p_31513_));
    }
    private double getHeadX(int p_31515_) {
        if (p_31515_ <= 0) {
            return this.getX();
        } else {
            float f = (this.yBodyRot + (float)(180 * (p_31515_ - 1))) * ((float)Math.PI / 180F);
            float f1 = Mth.cos(f);
            return this.getX() + (double)f1 * 1.3D;
        }
    }

    private double getHeadY(int p_31517_) {
        return p_31517_ <= 0 ? this.getY() + 3.0D : this.getY() + 2.2D;
    }

    private double getHeadZ(int p_31519_) {
        if (p_31519_ <= 0) {
            return this.getZ();
        } else {
            float f = (this.yBodyRot + (float)(180 * (p_31519_ - 1))) * ((float)Math.PI / 180F);
            float f1 = Mth.sin(f);
            return this.getZ() + (double)f1 * 1.3D;
        }
    }
    
    public void aiStep() {
        Vec3 vec3 = this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D);
        if (!this.level.isClientSide && this.getAlternativeTarget(0) > 0) {
            Entity entity = this.level.getEntity(this.getAlternativeTarget(0));
            if (entity != null) {
                double d0 = vec3.y;
                if (this.getY() < entity.getY() || !this.isPowered() && this.getY() < entity.getY() + 5.0D) {
                    d0 = Math.max(0.0D, d0);
                    d0 += 0.3D - d0 * (double)0.6F;
                }

                vec3 = new Vec3(vec3.x, d0, vec3.z);
                Vec3 vec31 = new Vec3(entity.getX() - this.getX(), 0.0D, entity.getZ() - this.getZ());
                if (vec31.horizontalDistanceSqr() > 9.0D) {
                    Vec3 vec32 = vec31.normalize();
                    vec3 = vec3.add(vec32.x * 0.3D - vec3.x * 0.6D, 0.0D, vec32.z * 0.3D - vec3.z * 0.6D);
                }
            }
        }

        this.setDeltaMovement(vec3);
        if (vec3.horizontalDistanceSqr() > 0.05D) {
            this.setYRot((float)Mth.atan2(vec3.z, vec3.x) * (180F / (float)Math.PI) - 90.0F);
        }

        super.aiStep();

        boolean flag = this.isPowered();

        double d8 = this.getHeadX(1);
        double d10 = this.getHeadY(1);
        double d2 = this.getHeadZ(1);
        if(flag)
        this.level.addParticle(ParticleTypes.EFFECT, d8 + this.random.nextGaussian() * (double)0.3F, d10 + this.random.nextGaussian() * (double)0.3F, d2 + this.random.nextGaussian() * (double)0.3F, (double)0.7F, (double)0.7F, 0.5D);
        
        if (this.getInvulnerableTicks() > 0) {
                this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + this.random.nextGaussian(), this.getY() + (double)(this.random.nextFloat() * 3.3F), this.getZ() + this.random.nextGaussian(), (double)0.7F, (double)0.7F, (double)0.9F);
        }

    }
    
    public void setAlternativeTarget(int p_31455_, int p_31456_) {
        this.entityData.set(DATA_TARGETS.get(p_31455_), p_31456_);
    }
    
    protected void customServerAiStep() {
        if (this.getInvulnerableTicks() > 0) {
            int k1 = this.getInvulnerableTicks() - 1;
            this.bossEvent.setProgress(1.0F - (float)k1 / 220.0F);
            this.setInvulnerableTicks(k1);
            if (this.tickCount % 10 == 0) {
                this.heal(10.0F);
            }
        }
        super.customServerAiStep();

        if (this.tickCount % 20 == 0) {
            this.heal(1.0F);
        }
        if(this.nextAttackUpdate <= 0) {
            performRangedAttack();
            Random r = new Random();
            int low = 50;
            int high = low+200;
            nextAttackUpdate = r.nextInt(high-low) + low;
        }
        nextAttackUpdate--;
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    public void makeInvulnerable() {
        this.setInvulnerableTicks(INVULNERABLE_TICKS);
        this.bossEvent.setProgress(0.0F);
        this.setHealth(this.getMaxHealth() / 3.0F);
    }

    private void performRangedAttack() {
        if (!this.isSilent()) {
            this.level.levelEvent((Player)null, 1024, this.blockPosition(), 0);
        }
        LivingEntity target = getTarget();
        if(target != null) {
            Vec3 vec3 = target.getDeltaMovement();
            double d0 = target.getX() + vec3.x - this.getX();
            double d1 = target.getEyeY() - 0.5F - this.getY();
            double d2 = target.getZ() + vec3.z - this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);

            KakaThrow kakaThrow = new KakaThrow(this.level, this);
            kakaThrow.setOwner(this);
            kakaThrow.setItem(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), Registration.KAKA_POTION.get()));
            kakaThrow.setXRot(kakaThrow.getXRot() - -20.0F);
            kakaThrow.shoot(d0, d1 + d3 * 0.2D, d2, 0.85F, 16.0F);
            this.swing(InteractionHand.MAIN_HAND);
            this.level.addFreshEntity(kakaThrow);
        }
    }
    public int getAttackAnimationTick() {
        return this.attackAnimationTick;
    }
    public boolean causeFallDamage(float p_149589_, float p_149590_, DamageSource p_149591_) {
        return false;
    }

    public boolean addEffect(MobEffectInstance p_182397_, @Nullable Entity p_182398_) {
        return false;
    }
    @Override
    public boolean isPowered() {
        return false;
    }
    public void addAdditionalSaveData(CompoundTag data) {
        super.addAdditionalSaveData(data);
        data.putInt("Invul", this.getInvulnerableTicks());
    }
    public void setInvulnerableTicks(int ticks) {
        this.entityData.set(DATA_ID_INV, ticks);
    }
    public void readAdditionalSaveData(CompoundTag data) {
        super.readAdditionalSaveData(data);
        this.setInvulnerableTicks(data.getInt("Invul"));
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }

    }


    public void setCustomName(@Nullable Component p_31476_) {
        super.setCustomName(p_31476_);
        this.bossEvent.setName(this.getDisplayName());
    }
    protected PathNavigation createNavigation(Level level) {
        GroundPathNavigation pathnavigation = new GroundPathNavigation(this, level);
        pathnavigation.setCanOpenDoors(false);
        pathnavigation.setCanFloat(true);
        pathnavigation.setCanPassDoors(true);
        return pathnavigation;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new KakaDemon.DoNothingGoal());
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 40, 20.0F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, false, false, LIVING_ENTITY_SELECTOR));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TARGET_A, 0);
        this.entityData.define(DATA_TARGET_B, 0);
        this.entityData.define(DATA_TARGET_C, 0);
        this.entityData.define(DATA_ID_INV, 0);
    }

    public int getInvulnerableTicks() {
        return this.entityData.get(DATA_ID_INV);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITHER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_31500_) {
        return SoundEvents.WITHER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_DEATH;
    }

    class DoNothingGoal extends Goal {
        public DoNothingGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            return KakaDemon.this.getInvulnerableTicks() > 0;
        }
    }
}
