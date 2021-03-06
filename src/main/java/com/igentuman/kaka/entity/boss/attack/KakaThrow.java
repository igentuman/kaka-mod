package com.igentuman.kaka.entity.boss.attack;


import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;

import com.igentuman.kaka.entity.ThrowKakaBlock;
import com.igentuman.kaka.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class KakaThrow extends ThrowableItemProjectile implements ItemSupplier {
    public static final double SPLASH_RANGE = 7.0D;
    private static final double SPLASH_RANGE_SQ = 22.0D;
    public static final Predicate<LivingEntity> WATER_SENSITIVE = LivingEntity::isSensitiveToWater;
    public LivingEntity owner;

    public KakaThrow(Level level, LivingEntity ent, double p_37531_, double p_37532_, double p_37533_) {
        super(EntityType.POTION, p_37531_, p_37532_, p_37533_, level);
        owner = ent;
    }
    public KakaThrow(EntityType<? extends ThrowableItemProjectile> p_37527_, Level p_37528_) {
        super(p_37527_, p_37528_);
    }

    public KakaThrow(Level level, LivingEntity p_37536_) {
        super(Registration.THROW_KAKA_BLOCK.get(), p_37536_, level);
    }

    public KakaThrow(Level level, double p_37531_, double p_37532_, double p_37533_) {
        super(EntityType.POTION, p_37531_, p_37532_, p_37533_, level);
    }

    protected Item getDefaultItem() {
        return Registration.KAKA_BLOCK_ITEM.get();
    }

    protected float getGravity() {
        return 0.05F;
    }

    protected void onHitBlock(BlockHitResult p_37541_) {
        super.onHitBlock(p_37541_);
        if (!this.level.isClientSide) {
            ItemStack itemstack = this.getItem();
            Potion potion = PotionUtils.getPotion(itemstack);
            List<MobEffectInstance> list = PotionUtils.getMobEffects(itemstack);
            boolean flag = potion == Potions.WATER && list.isEmpty();
            Direction direction = p_37541_.getDirection();
            BlockPos blockpos = p_37541_.getBlockPos();
            BlockPos blockpos1 = blockpos.relative(direction);
            if (flag) {
                this.dowseFire(blockpos1);
                this.dowseFire(blockpos1.relative(direction.getOpposite()));

                for(Direction direction1 : Direction.Plane.HORIZONTAL) {
                    this.dowseFire(blockpos1.relative(direction1));
                }
            }

        }
    }

    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            ItemStack itemstack = this.getItem();
            List<MobEffectInstance> list = PotionUtils.getMobEffects(itemstack);
            this.applySplash(list, result.getType() == HitResult.Type.ENTITY ? ((EntityHitResult)result).getEntity() : null);
            this.level.levelEvent(2002, this.blockPosition(), PotionUtils.getColor(itemstack));
            this.discard();
        }
    }

    private void applyWater() {
        AABB aabb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
        List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, aabb, WATER_SENSITIVE);
        if (!list.isEmpty()) {
            for(LivingEntity livingentity : list) {
                double d0 = this.distanceToSqr(livingentity);
                if (d0 < 16.0D && livingentity.isSensitiveToWater()) {
                    livingentity.hurt(DamageSource.indirectMagic(this, this.getOwner()), 1.0F);
                }
            }
        }

        for(Axolotl axolotl : this.level.getEntitiesOfClass(Axolotl.class, aabb)) {
            axolotl.rehydrate();
        }

    }

    private void applySplash(List<MobEffectInstance> p_37548_, @Nullable Entity p_37549_) {
        AABB aabb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
        List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, aabb);
        if (!list.isEmpty()) {
            Entity entity = this.getEffectSource();

            for(LivingEntity livingentity : list) {
                if (livingentity.isAffectedByPotions()) {
                    double d0 = this.distanceToSqr(livingentity);
                    if (d0 < 16.0D) {
                        double d1 = 1.0D - Math.sqrt(d0) / 4.0D;
                        if (livingentity == p_37549_) {
                            d1 = 1.0D;
                        }

                        for(MobEffectInstance mobeffectinstance : p_37548_) {
                            MobEffect mobeffect = mobeffectinstance.getEffect();
                            if (mobeffect.isInstantenous()) {
                                mobeffect.applyInstantenousEffect(this, this.getOwner(), livingentity, mobeffectinstance.getAmplifier(), d1);
                            } else {
                                int i = (int)(d1 * (double)mobeffectinstance.getDuration() + 0.5D);
                                if (i > 20) {
                                    livingentity.addEffect(new MobEffectInstance(mobeffect, i, mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible()), entity);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private void makeAreaOfEffectCloud(ItemStack p_37538_, Potion p_37539_) {
        AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity) {
            areaeffectcloud.setOwner((LivingEntity)entity);
        }

        areaeffectcloud.setRadius(3.0F);
        areaeffectcloud.setRadiusOnUse(-0.5F);
        areaeffectcloud.setWaitTime(10);
        areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float)areaeffectcloud.getDuration());
        areaeffectcloud.setPotion(p_37539_);

        for(MobEffectInstance mobeffectinstance : PotionUtils.getCustomEffects(p_37538_)) {
            areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
        }

        CompoundTag compoundtag = p_37538_.getTag();
        if (compoundtag != null && compoundtag.contains("CustomPotionColor", 99)) {
            areaeffectcloud.setFixedColor(compoundtag.getInt("CustomPotionColor"));
        }

        this.level.addFreshEntity(areaeffectcloud);
    }

    private boolean isLingering() {
        return this.getItem().is(Items.LINGERING_POTION);
    }

    private void dowseFire(BlockPos p_150193_) {
        BlockState blockstate = this.level.getBlockState(p_150193_);
        if (blockstate.is(BlockTags.FIRE)) {
            this.level.removeBlock(p_150193_, false);
        } else if (AbstractCandleBlock.isLit(blockstate)) {
            AbstractCandleBlock.extinguish((Player)null, blockstate, this.level, p_150193_);
        } else if (CampfireBlock.isLitCampfire(blockstate)) {
            this.level.levelEvent((Player)null, 1009, p_150193_, 0);
            CampfireBlock.dowse(this.getOwner(), this.level, p_150193_, blockstate);
            this.level.setBlockAndUpdate(p_150193_, blockstate.setValue(CampfireBlock.LIT, Boolean.valueOf(false)));
        }

    }
}