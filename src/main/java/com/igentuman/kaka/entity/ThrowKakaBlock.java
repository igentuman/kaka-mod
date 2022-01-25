package com.igentuman.kaka.entity;

import com.igentuman.kaka.setup.Registration;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class ThrowKakaBlock extends ThrowableItemProjectile {

    public ThrowKakaBlock(EntityType<? extends ThrowKakaBlock> p_36721_, Level p_36722_) {
        super(p_36721_, p_36722_);
    }

    @Override
    protected Item getDefaultItem() {
        return Registration.KAKA_BLOCK_ITEM.get();
    }
}
