package com.igentuman.kaka.block;

import com.igentuman.kaka.entity.boss.KakaDemon;
import com.igentuman.kaka.setup.Registration;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockMaterialPredicate;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;

public class KakaDemonHeadBlock extends HorizontalDirectionalBlock {
    
    private static BlockPattern kakaDemonPatternFull;

    public KakaDemonHeadBlock(Properties props) {
        super(props);
    }

    public void setPlacedBy(Level p_58260_, BlockPos p_58261_, BlockState p_58262_, @Nullable LivingEntity p_58263_, ItemStack p_58264_) {
        super.setPlacedBy(p_58260_, p_58261_, p_58262_, p_58263_, p_58264_);
        checkSpawn(p_58260_, p_58261_);
    }

    public static void checkSpawn(Level p_58256_, BlockPos p_58257_) {
        if (!p_58256_.isClientSide) {
            if (p_58257_.getY() >= p_58256_.getMinBuildHeight() && p_58256_.getDifficulty() != Difficulty.PEACEFUL) {
                BlockPattern blockpattern = getOrCreateKakaDemonFull();
                BlockPattern.BlockPatternMatch blockpattern$blockpatternmatch = blockpattern.find(p_58256_, p_58257_);
                if (blockpattern$blockpatternmatch != null) {
                    for(int i = 0; i < blockpattern.getWidth(); ++i) {
                        for(int j = 0; j < blockpattern.getHeight(); ++j) {
                            BlockInWorld blockinworld = blockpattern$blockpatternmatch.getBlock(i, j, 0);
                            p_58256_.setBlock(blockinworld.getPos(), Blocks.AIR.defaultBlockState(), 2);
                            p_58256_.levelEvent(2001, blockinworld.getPos(), Block.getId(blockinworld.getState()));
                        }
                    }

                    KakaDemon kakaDemon = Registration.KAKA_DEMON.get().create(p_58256_);
                    BlockPos blockpos = blockpattern$blockpatternmatch.getBlock(1, 2, 0).getPos();
                    kakaDemon.moveTo((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.55D, (double)blockpos.getZ() + 0.5D, blockpattern$blockpatternmatch.getForwards().getAxis() == Direction.Axis.X ? 0.0F : 90.0F, 0.0F);
                    kakaDemon.yBodyRot = blockpattern$blockpatternmatch.getForwards().getAxis() == Direction.Axis.X ? 0.0F : 90.0F;
                    kakaDemon.makeInvulnerable();

                    for(ServerPlayer serverplayer : p_58256_.getEntitiesOfClass(ServerPlayer.class, kakaDemon.getBoundingBox().inflate(50.0D))) {
                        CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayer, kakaDemon);
                    }

                    p_58256_.addFreshEntity(kakaDemon);

                    for(int k = 0; k < blockpattern.getWidth(); ++k) {
                        for(int l = 0; l < blockpattern.getHeight(); ++l) {
                            p_58256_.blockUpdated(blockpattern$blockpatternmatch.getBlock(k, l, 0).getPos(), Blocks.AIR);
                        }
                    }

                }
            }
        }
    }

    private static BlockPattern getOrCreateKakaDemonFull() {
        if (kakaDemonPatternFull == null) {
            kakaDemonPatternFull = BlockPatternBuilder.start().aisle("~~^~~", "#####", "#~#~#","~~#~~").where('#', (p_58272_) -> {
                return p_58272_.getState().is(Registration.KAKA_BLOCK.get());
            }).where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(Registration.KAKA_DEMON_HEAD_BLOCK.get()))).where('~', BlockInWorld.hasState(BlockMaterialPredicate.forMaterial(Material.AIR))).build();
        }

        return kakaDemonPatternFull;
    }
}
