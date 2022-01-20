package com.igentuman.kaka.datagen;

import com.igentuman.kaka.Kaka;
import com.igentuman.kaka.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class KakaTags extends ItemTagsProvider {

    public KakaTags(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, new BlockTagsProvider(generator,Kaka.MODID,helper), Kaka.MODID, helper);
    }

    @Override
    protected void addTags() {
        tag(Registration.KAKA_ITEM)
                .add(Registration.COW_KAKA.get())
                .add(Registration.SHEEP_KAKA.get())
                .add(Registration.HORSE_KAKA.get())
                .add(Registration.VILAGER_KAKA.get())
                .add(Registration.PLAYER_KAKA.get())
                .add(Registration.PIG_KAKA.get());
    }

    @Override
    public String getName() {
        return "Kaka Tags";
    }
}