package com.igentuman.kaka.datagen;

import com.igentuman.kaka.Kaka;
import com.igentuman.kaka.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class KakaBlockstates extends BlockStateProvider {

    public KakaBlockstates(DataGenerator gen, ExistingFileHelper helper) {
        super(gen, Kaka.MODID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(Registration.KAKA_BLOCK.get());
        Block block = Registration.KAKA_DEMON_HEAD_BLOCK.get();
        ResourceLocation face = modLoc("block/kaka_demon_face");
        ResourceLocation side = modLoc("block/kaka_demon_side");
        ResourceLocation top = modLoc("block/kaka_demon_top");
        ResourceLocation bottom = modLoc("block/kaka_demon_bottom");
        simpleBlock(block, models().cube(block.getRegistryName().getPath(), bottom, top, face, side, side, side));
    }

}
