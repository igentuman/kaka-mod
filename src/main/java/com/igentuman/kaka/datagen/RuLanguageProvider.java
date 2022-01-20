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
        add("itemGroup." + TAB_NAME, "????");
        add(Registration.COW_KAKA.get(), "??????? ????");
        add(Registration.SHEEP_KAKA.get(), "?????? ????");
        add(Registration.HORSE_KAKA.get(), "????? ?????????? ????");
        add(Registration.PIG_KAKA.get(), "????? ???????? ????");
        add(Registration.VILAGER_KAKA.get(), "????? ???? ?????");
        add(Registration.PLAYER_KAKA.get(), "????? ???? ??????");
        add(Registration.KAKA_EFFECT.get(), "?????? ????");
        add("item.minecraft.potion.effect.kaka", "Kaka ?????");
        add("item.minecraft.splash_potion.effect.kaka", "???????? ???? ?????");
        add("item.minecraft.lingering_potion.effect.kaka", "?\\???????? Kaka ?????");
    }
}