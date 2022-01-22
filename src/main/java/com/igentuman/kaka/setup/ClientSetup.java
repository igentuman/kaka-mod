package com.igentuman.kaka.setup;

import com.igentuman.kaka.Kaka;
import com.igentuman.kaka.entity.boss.KakaDemon;
import com.igentuman.kaka.entity.render.KakaDemonRender;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
@Mod.EventBusSubscriber(modid = Kaka.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    public static void init(FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
           // EntityRenderers.register(Registration.KAKA_DEMON.get(), KakaDemonRender::new);
        });
    }



    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
       // event.registerLayerDefinition(KakaDemonModel., KakaDemonModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(Registration.KAKA_DEMON.get(), KakaDemonRender::new);
    }

    @SubscribeEvent
    public static void entityAttributeCreationEvent(EntityAttributeCreationEvent event) {
        event.put(Registration.KAKA_DEMON.get(), KakaDemon.prepareAttributes().build());
    }
}
