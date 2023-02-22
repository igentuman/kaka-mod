package com.igentuman.kaka.entity.render;

import com.igentuman.kaka.entity.boss.KakaDemon;
import com.igentuman.kaka.entity.model.KakaDemonModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.igentuman.kaka.Kaka.MODID;

@OnlyIn(Dist.CLIENT)
public class KakaDemonRender extends MobRenderer<KakaDemon, KakaDemonModel<KakaDemon>> {
    private static final ResourceLocation DEMON_LOCATION = new ResourceLocation(MODID,"textures/entity/kaka_demon/kaka_demon.png");

    public KakaDemonRender(EntityRendererProvider.Context p_174188_) {
        super(p_174188_, new KakaDemonModel<>(p_174188_.bakeLayer(ModelLayers.IRON_GOLEM)), 0.7F);
       // this.addLayer(new KakaDemonCrackinessLayer(this));
    }

    public ResourceLocation getTextureLocation(KakaDemon p_115012_) {
        return DEMON_LOCATION;
    }

    protected void setupRotations(KakaDemon p_115014_, PoseStack p_115015_, float p_115016_, float p_115017_, float p_115018_) {
        super.setupRotations(p_115014_, p_115015_, p_115016_, p_115017_, p_115018_);
        if (!((double)p_115014_.animationSpeed < 0.01D)) {
            float f = 13.0F;
            float f1 = p_115014_.animationPosition - p_115014_.animationSpeed * (1.0F - p_115018_) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            p_115015_.mulPose(Axis.ZP.rotationDegrees(6.5F * f2));
        }
    }
}