package com.igentuman.kaka.datagen;

import com.igentuman.kaka.Kaka;
import com.igentuman.kaka.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import static com.igentuman.kaka.setup.ModSetup.TAB_NAME;

public class RuLanguageProvider extends LanguageProvider {

    public RuLanguageProvider(DataGenerator gen, String locale) {
        super(gen, Kaka.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + TAB_NAME, "Кака");
        add(Registration.COW_KAKA.get(), "Кучка Коровьего Кака");
        add(Registration.SHEEP_KAKA.get(), "Кучка Овечьего Кака");
        add(Registration.HORSE_KAKA.get(), "Кучка Лошадиного Кака");
    }
}