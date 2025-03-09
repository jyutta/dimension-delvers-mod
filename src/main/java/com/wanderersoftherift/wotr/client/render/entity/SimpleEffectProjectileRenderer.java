package com.wanderersoftherift.wotr.client.render.entity;

import com.wanderersoftherift.wotr.client.model.SimpleEffectProjectileModel;
import com.wanderersoftherift.wotr.client.render.entity.state.SimpleEffectProjectileRenderState;
import com.wanderersoftherift.wotr.entity.projectile.SimpleEffectProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SimpleEffectProjectileRenderer extends EntityRenderer<SimpleEffectProjectile, SimpleEffectProjectileRenderState> {
    private final SimpleEffectProjectileModel model;

    public SimpleEffectProjectileRenderer(EntityRendererProvider.Context p_173917_) {
        super(p_173917_);
        this.model = new SimpleEffectProjectileModel(p_173917_.bakeLayer(ModelLayers.ARROW));
    }

    @Override
    public void render(SimpleEffectProjectileRenderState renderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(renderState.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(renderState.xRot));
        VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RenderType.entityCutout(this.getTextureLocation(renderState)));
        this.model.setupAnim(renderState);
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(renderState, poseStack, multiBufferSource, packedLight);
    }

    @Override
    public SimpleEffectProjectileRenderState createRenderState() {
        return new SimpleEffectProjectileRenderState();
    }

    public ResourceLocation getTextureLocation(SimpleEffectProjectileRenderState renderState){
        return renderState.texture;
    }

    public void extractRenderState(SimpleEffectProjectile projectile, SimpleEffectProjectileRenderState state, float partialTick) {
        super.extractRenderState(projectile, state, partialTick);
        state.xRot = projectile.getXRot(partialTick);
        state.yRot = projectile.getYRot(partialTick);
        state.shake = (float)projectile.shakeTime - partialTick;
        state.texture = projectile.getTexture();
    }
}
