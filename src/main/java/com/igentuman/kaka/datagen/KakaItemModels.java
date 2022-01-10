package com.igentuman.kaka.datagen;

import com.igentuman.kaka.Kaka;
import com.igentuman.kaka.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class KakaItemModels extends ItemModelProvider {
    public KakaItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Kaka.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        singleTexture(Registration.KAKA.get().getRegistryName().getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("item/kaka"));
    }
}
