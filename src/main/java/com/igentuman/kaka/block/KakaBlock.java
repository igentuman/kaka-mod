package com.igentuman.kaka.block;

import com.igentuman.kaka.config.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HoneyBlock;
import net.minecraft.world.level.block.state.BlockState;

public class KakaBlock extends HoneyBlock {
    public KakaBlock(Properties props) {
        super(props);
    }

    public void stepOn(Level level, BlockPos pos, BlockState blockState, Entity entity) {
        if (entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity)) {
            if(CommonConfig.GENERAL.kaka_block_damage.get() > 0)
            entity.hurt(DamageSource.MAGIC, CommonConfig.GENERAL.kaka_block_damage.get());
        }
        super.stepOn(level, pos, blockState, entity);
    }
}
