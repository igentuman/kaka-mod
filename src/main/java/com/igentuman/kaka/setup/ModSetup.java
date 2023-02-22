package com.igentuman.kaka.setup;

import com.igentuman.kaka.Kaka;
import com.igentuman.kaka.entity.boss.KakaDemon;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static com.igentuman.kaka.Kaka.MODID;

@Mod.EventBusSubscriber(modid = Kaka.MODID)
public class ModSetup {

    public final static String TAB_NAME = MODID;

    public static void init(FMLCommonSetupEvent event)
    {
        BrewingRecipeRegistry.addRecipe(
                Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)),
                Ingredient.of(Registration.COW_KAKA.get(),Registration.VILAGER_KAKA.get(),Registration.PLAYER_KAKA.get(),Registration.PIG_KAKA.get(),Registration.HORSE_KAKA.get(),Registration.SHEEP_KAKA.get()),
                PotionUtils.setPotion(new ItemStack(Items.POTION), Registration.KAKA_POTION.get()));

    }

    @SubscribeEvent
    public static void entityAttributeCreationEvent(EntityAttributeCreationEvent event) {
        event.put(Registration.KAKA_DEMON.get(), KakaDemon.prepareAttributes().build());
    }

    @SubscribeEvent
    public static void onCustomTab(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(Kaka.MODID, "tutorial"), builder -> {
            builder.title(Component.translatable("itemGroup." + TAB_NAME))
                    .icon(() -> new ItemStack(Registration.COW_KAKA.get()))
                    .displayItems((enabledFeatures, output, tab) -> {
                        output.accept(Registration.KAKA_BLOCK.get());
                        output.accept(Registration.KAKA_BLOCK.get());

                    });
        });
    }
}
