package com.igentuman.kaka.datagen;

import com.igentuman.kaka.setup.Registration;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.BiConsumer;

public class KakaLootTable implements LootTableSubProvider {
    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> builder) {
        builder.accept(Registration.KAKA_BLOCK.getId(), createSimpleTable("kaka", Registration.KAKA_BLOCK.get()));
        builder.accept(Registration.KAKA_DEMON_HEAD_BLOCK.getId(), createSimpleTable("kaka_head", Registration.KAKA_DEMON_HEAD_BLOCK.get()));
    }

    public static LootTable.Builder createSimpleTable(String name, Block block) {
        LootPool.Builder builder = LootPool.lootPool()
                .name(name)
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(block));
        return LootTable.lootTable().withPool(builder);
    }
}
