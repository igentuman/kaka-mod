package com.igentuman.kaka.datagen;

import com.igentuman.kaka.setup.Registration;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

import java.util.function.Consumer;

public class KakaRecipes extends RecipeProvider {

    public KakaRecipes(PackOutput generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.KAKA_BLOCK_ITEM.get())
                .pattern("ccc")
                .pattern("ccc")
                .pattern("ccc")
                .define('c', Registration.KAKA_ITEM)
                .unlockedBy("dirt", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.COW_KAKA.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.KAKA_DEMON_HEAD_BLOCK_ITEM.get())
                .pattern("ccc")
                .pattern("csc")
                .pattern("ccc")
                .define('c', Registration.KAKA_BLOCK_ITEM.get())
                .define('s', Items.WITHER_SKELETON_SKULL)
                .unlockedBy("dirt", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.KAKA_BLOCK_ITEM.get()))
                .save(consumer);
    }
}
