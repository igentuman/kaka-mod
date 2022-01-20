package com.igentuman.kaka.setup;

import com.igentuman.kaka.Kaka;
import com.igentuman.kaka.effect.KakaEffect;
import com.igentuman.kaka.item.KakaItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import static com.igentuman.kaka.Kaka.MODID;

public class Registration {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(ModSetup.ITEM_GROUP);
    public static final BlockBehaviour.Properties BLOCK_PROPERTIES = BlockBehaviour.Properties.of(Material.DIRT).strength(2f);

    public static final RegistryObject<Block> KAKA_BLOCK = BLOCKS.register("kaka_block", () -> new Block(BLOCK_PROPERTIES));
    public static final RegistryObject<Item> KAKA_BLOCK_ITEM = fromBlock(KAKA_BLOCK);

    public static final RegistryObject<Item> COW_KAKA       = registerItem("cow_kaka");
    public static final RegistryObject<Item> SHEEP_KAKA     = registerItem("sheep_kaka");
    public static final RegistryObject<Item> HORSE_KAKA     = registerItem("horse_kaka");
    public static final RegistryObject<Item> VILAGER_KAKA   = registerItem("vilager_kaka");
    public static final RegistryObject<Item> PLAYER_KAKA    = registerItem("player_kaka");
    public static final RegistryObject<Item> PIG_KAKA       = registerItem("pig_kaka");

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Kaka.MODID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, Kaka.MODID);

    public static final RegistryObject<MobEffect> KAKA_EFFECT = EFFECTS.register("kaka", () -> new KakaEffect(MobEffectCategory.HARMFUL, 0xd4ff00));
    public static final RegistryObject<Potion> KAKA_POTION = POTIONS.register("kaka", () -> new Potion(new MobEffectInstance(KAKA_EFFECT.get(), 3600)));

    public static void init()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        EFFECTS.register(bus);
        POTIONS.register(bus);
    }

    public static final Tags.IOptionalNamedTag<Item> KAKA_ITEM = ItemTags.createOptional(new ResourceLocation(Kaka.MODID, "kaka_item"));

    public static RegistryObject<Item> registerItem(String name)
    {
        return ITEMS.register(name, () -> new KakaItem(ITEM_PROPERTIES));
    }

    public static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), ITEM_PROPERTIES));
    }
}
