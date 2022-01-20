package com.igentuman.kaka.datagen;

import com.igentuman.kaka.Kaka;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Kaka.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            generator.addProvider(new KakaTags(generator, event.getExistingFileHelper()));
            generator.addProvider(new KakaRecipes(generator));
        }
        if (event.includeClient()) {
            generator.addProvider(new KakaBlockstates(generator, event.getExistingFileHelper()));
            generator.addProvider(new KakaItemModels(generator, event.getExistingFileHelper()));
            generator.addProvider(new EnLanguageProvider(generator, "en_us"));
            generator.addProvider(new RuLanguageProvider(generator, "ru_ru"));
        }
    }
}