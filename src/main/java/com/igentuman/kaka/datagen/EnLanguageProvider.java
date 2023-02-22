package com.igentuman.kaka.datagen;

import com.igentuman.kaka.Kaka;
import com.igentuman.kaka.setup.Registration;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

import static com.igentuman.kaka.setup.ModSetup.TAB_NAME;

public class EnLanguageProvider extends LanguageProvider {

    public EnLanguageProvider(PackOutput gen, String locale) {
        super(gen, Kaka.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + TAB_NAME, "Kaka");
        add(Registration.COW_KAKA.get(), "Pile of cow's Kaka");
        add(Registration.SHEEP_KAKA.get(), "Pile of sheep's Kaka");
        add(Registration.HORSE_KAKA.get(), "Pile of horse's Kaka");
        add(Registration.PIG_KAKA.get(), "Pile of pigs's Kaka");
        add(Registration.VILAGER_KAKA.get(), "Pile of vilager's Kaka");
        add(Registration.PLAYER_KAKA.get(), "Pile of player's Kaka");
        add(Registration.KAKA_BLOCK_ITEM.get(), "Kaka Block");
        add(Registration.KAKA_DEMON_HEAD_BLOCK_ITEM.get(), "Kaka Demon Head Block (WIP)");
        add(Registration.KAKA_EFFECT.get(), "Kaka Effect");
        add("item.minecraft.potion.effect.kaka", "Kaka Potion");
        add("item.minecraft.splash_potion.effect.kaka", "Splash Kaka Potion");
        add("item.minecraft.lingering_potion.effect.kaka", "Splash Kaka Potion");
    }
}