package com.igentuman.kaka.datagen;

import com.igentuman.kaka.Kaka;
import com.igentuman.kaka.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class KakaBlockstates extends BlockStateProvider {

    public KakaBlockstates(DataGenerator gen, ExistingFileHelper helper) {
        super(gen, Kaka.MODID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(Registration.KAKA_BLOCK.get());
    }
}
