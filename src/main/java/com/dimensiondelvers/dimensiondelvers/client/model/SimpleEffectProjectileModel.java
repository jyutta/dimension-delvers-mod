package com.dimensiondelvers.dimensiondelvers.client.model;

import com.dimensiondelvers.dimensiondelvers.client.render.entity.state.SimpleEffectProjectileRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

public class SimpleEffectProjectileModel extends EntityModel<SimpleEffectProjectileRenderState> {

    public SimpleEffectProjectileModel(ModelPart root) {
        super(root, RenderType::entityCutout);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild(
                "back",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -2.5F, -2.5F, 0.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(-11.0F, 0.0F, 0.0F, (float) (Math.PI / 4), 0.0F, 0.0F).withScale(0.8F)
        );
        CubeListBuilder cubelistbuilder = CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-12.0F, -2.0F, 0.0F, 16.0F, 4.0F, 0.0F, CubeDeformation.NONE, 1.0F, 0.8F);
        partdefinition.addOrReplaceChild("cross_1", cubelistbuilder, PartPose.rotation((float) (Math.PI / 4), 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("cross_2", cubelistbuilder, PartPose.rotation((float) (Math.PI * 3.0 / 4.0), 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition.transformed(p_360450_ -> p_360450_.scaled(0.9F)), 32, 32);
    }

    public void setupAnim(SimpleEffectProjectileRenderState p_365522_) {
        super.setupAnim(p_365522_);
        if (p_365522_.shake > 0.0F) {
            float f = -Mth.sin(p_365522_.shake * 3.0F) * p_365522_.shake;
            this.root.zRot += f * (float) (Math.PI / 180.0);
        }
    }
}