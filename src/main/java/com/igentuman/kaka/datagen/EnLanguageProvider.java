package com.igentuman.kaka.datagen;

import com.igentuman.kaka.Kaka;
import com.igentuman.kaka.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import static com.igentuman.kaka.setup.ModSetup.TAB_NAME;

public class EnLanguageProvider extends LanguageProvider {

    public EnLanguageProvider(DataGenerator gen, String locale) {
        super(gen, Kaka.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + TAB_NAME, "Kaka");
        add(Registration.KAKA.get(), "Pile of Kaka");
    }
}