package com.igentuman.kaka.entity.render;

import com.igentuman.kaka.entity.boss.KakaDemon;
import com.igentuman.kaka.entity.model.KakaDemonModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.igentuman.kaka.Kaka.MODID;

@OnlyIn(Dist.CLIENT)
public class KakaDemonRender extends MobRenderer<KakaDemon, KakaDemonModel<KakaDemon>> {
    private static final ResourceLocation DEMON_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID,"textures/entity/kaka_demon/kaka_demon.png");
    private static final ResourceLocation DEMON_GLOW_TEXTURE = ResourceLocation.fromNamespaceAndPath(MODID,"textures/entity/kaka_demon/kaka_demon_glow.png");

    private final RandomSource random = RandomSource.create();

    public KakaDemonRender(EntityRendererProvider.Context context) {
        super(context, new KakaDemonModel<>(context.bakeLayer(KakaDemonModel.LAYER_LOCATION)), 0.7F);
        this.addLayer(new KakaDemonGlowLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(KakaDemon p_115012_) {
        return DEMON_LOCATION;
    }

    @Override
    public void render(KakaDemon entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        // Scale the entity to make it look more imposing
        poseStack.pushPose();
        poseStack.scale(1.5F, 1.5F, 1.5F);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        poseStack.popPose();
    }



    protected void setupRotations(KakaDemon p_115014_, PoseStack p_115015_, float p_115016_, float p_115017_, float p_115018_) {
        super.setupRotations(p_115014_, p_115015_, p_115016_, p_115017_, p_115018_);
        if (!((double)p_115014_.walkAnimation.speed() < 0.01D)) {
            float f = 13.0F;
            float f1 = p_115014_.walkAnimation.position() - p_115014_.walkAnimation.speed() * (1.0F - p_115018_) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            p_115015_.mulPose(Axis.ZP.rotationDegrees(6.5F * f2));
        }
    }

    /**
     * Glow layer for the kaka demon to make it look more menacing
     */
    private static class KakaDemonGlowLayer extends RenderLayer<KakaDemon, KakaDemonModel<KakaDemon>> {

        public KakaDemonGlowLayer(KakaDemonRender renderer) {
            super(renderer);
        }

        @Override
        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn,
                            KakaDemon entitylivingbaseIn, float limbSwing, float limbSwingAmount,
                            float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

            if (!entitylivingbaseIn.isInvisible()) {
                KakaDemonModel<KakaDemon> model = this.getParentModel();

                float glowIntensity = 0.5F + 0.5F * (float) Math.sin((entitylivingbaseIn.tickCount + partialTicks) * 0.1F);
                int glowLight = 15728880; // Full bright (15 << 20 | 15 << 4)

                model.renderToBuffer(
                    matrixStackIn,
                    bufferIn.getBuffer(RenderType.eyes(DEMON_GLOW_TEXTURE)), // Use eyes render type for glow effect
                    glowLight,
                    packedLightIn,
                    1.0F,
                    glowIntensity,
                    glowIntensity,
                    1.0F
                );
            }
        }
    }
}