package com.igentuman.kaka.setup;

import com.igentuman.kaka.entity.goal.KakaGoal;
import com.igentuman.kaka.item.KakaItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import static com.igentuman.kaka.Kaka.MODID;

public class Registration {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(ModSetup.ITEM_GROUP);

    public static void init()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }

    public static final RegistryObject<Item> COW_KAKA = ITEMS.register("cow_kaka", () -> new KakaItem(ITEM_PROPERTIES));
    public static final RegistryObject<Item> SHEEP_KAKA = ITEMS.register("sheep_kaka", () -> new KakaItem(ITEM_PROPERTIES));
    public static final RegistryObject<Item> HORSE_KAKA = ITEMS.register("horse_kaka", () -> new KakaItem(ITEM_PROPERTIES));

    public static RegistryObject<Item> registerItem(String name)
    {
        return ITEMS.register(name, () -> new Item(ITEM_PROPERTIES));
    }
}
