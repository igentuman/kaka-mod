package com.igentuman.kaka.setup;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class Items {
    public static final RegistryObject<Item> KAKA = Registration.ITEMS.register("kaka", () -> new Item(Registration.ITEM_PROPERTIES));
}
