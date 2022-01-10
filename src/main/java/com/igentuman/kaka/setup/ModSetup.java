package com.igentuman.kaka.setup;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static com.igentuman.kaka.Kaka.MODID;

public class ModSetup {

    public final static String TAB_NAME = MODID;

    public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(TAB_NAME) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(net.minecraft.world.item.Items.DIAMOND);
        }
    };

    public static void init(FMLCommonSetupEvent event)
    {

    }
}
