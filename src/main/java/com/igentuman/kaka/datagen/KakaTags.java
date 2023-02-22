package com.igentuman.kaka.datagen;

import com.igentuman.kaka.Kaka;
import com.igentuman.kaka.setup.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class KakaTags extends ItemTagsProvider {

    public KakaTags(PackOutput generator, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTags, ExistingFileHelper helper) {
        super(generator, lookupProvider, blockTags, Kaka.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
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