package com.igentuman.kaka.datagen;

import com.igentuman.kaka.Kaka;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Kaka.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        KakaBlockTags blockTags = new KakaBlockTags(packOutput, lookupProvider, event.getExistingFileHelper());
        if (event.includeServer()) {
            generator.addProvider(true, new LootTableProvider(packOutput, Collections.emptySet(),
                    List.of(new LootTableProvider.SubProviderEntry(KakaLootTable::new, LootContextParamSets.BLOCK))));
            generator.addProvider(true, new KakaTags(packOutput, lookupProvider, blockTags, event.getExistingFileHelper()));
            generator.addProvider(true, new KakaRecipes(packOutput));
        }
        if (event.includeClient()) {
            generator.addProvider(true, new KakaBlockstates(packOutput, event.getExistingFileHelper()));
            generator.addProvider(true, new KakaItemModels(packOutput, event.getExistingFileHelper()));
            generator.addProvider(true, new EnLanguageProvider(packOutput, "en_us"));
            generator.addProvider(true, new RuLanguageProvider(packOutput, "ru_ru"));
        }
    }
}