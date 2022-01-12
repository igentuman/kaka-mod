package com.igentuman.kaka.datagen;

import com.igentuman.kaka.setup.Registration;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class KakaRecipes extends RecipeProvider {

    public KakaRecipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {

        ShapedRecipeBuilder.shaped(Registration.KAKA.get())
                .pattern("   ")
                .pattern(" # ")
                .pattern("#x#")
                .define('x', Items.MUSHROOM_STEW)
                .define('#', Blocks.DIRT)
                .unlockedBy("dirt", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MUSHROOM_STEW))
                .save(consumer);

    }
}
