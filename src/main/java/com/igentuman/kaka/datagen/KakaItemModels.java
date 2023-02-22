package com.igentuman.kaka.datagen;

import com.igentuman.kaka.Kaka;
import com.igentuman.kaka.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class KakaItemModels extends ItemModelProvider {
    public KakaItemModels(PackOutput generator, ExistingFileHelper existingFileHelper) {
        super(generator, Kaka.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        singleTexture(Registration.COW_KAKA.getId().getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("item/cow_kaka"));

        singleTexture(Registration.SHEEP_KAKA.getId().getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("item/sheep_kaka"));

        singleTexture(Registration.HORSE_KAKA.getId().getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("item/horse_kaka"));

        singleTexture(Registration.PIG_KAKA.getId().getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("item/pig_kaka"));

        singleTexture(Registration.VILAGER_KAKA.getId().getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("item/vilager_kaka"));

        singleTexture(Registration.PLAYER_KAKA.getId().getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("item/player_kaka"));

        withExistingParent(Registration.KAKA_DEMON_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));

        withExistingParent(Registration.KAKA_BLOCK_ITEM.getId().getPath(), modLoc("block/kaka_block"));

        withExistingParent(Registration.KAKA_DEMON_HEAD_BLOCK_ITEM.getId().getPath(), modLoc("block/kaka_demon_head_block"));

    }
}
