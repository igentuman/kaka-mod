package com.igentuman.kaka;

import com.igentuman.kaka.config.CommonConfig;
import com.igentuman.kaka.setup.ClientSetup;
import com.igentuman.kaka.setup.Events;
import com.igentuman.kaka.setup.ModSetup;
import com.igentuman.kaka.setup.Registration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Kaka.MODID)
public class Kaka
{
    public static final String MODID = "kaka";

    private static final Logger LOGGER = LogManager.getLogger();

    public Kaka() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.spec);
        Registration.init();
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        modbus.addListener(ModSetup::init);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,() -> () -> modbus.addListener(ClientSetup::init));
        modbus.register(new Events());
    }

    private void onModConfigEvent(final ModConfigEvent event) {
        if (event.getConfig().getType() == ModConfig.Type.COMMON)
            CommonConfig.setLoaded();
    }

}
