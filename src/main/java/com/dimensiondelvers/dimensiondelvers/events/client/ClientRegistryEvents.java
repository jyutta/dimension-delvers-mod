package com.dimensiondelvers.dimensiondelvers.events.client;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.client.render.entity.SimpleEffectProjectileRenderer;
import com.dimensiondelvers.dimensiondelvers.client.render.item.properties.select.SelectRuneGemShape;
import com.dimensiondelvers.dimensiondelvers.client.tooltip.ImageTooltipRenderer;
import com.dimensiondelvers.dimensiondelvers.init.ModEntities;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterSelectItemModelPropertyEvent;

@EventBusSubscriber(modid = DimensionDelvers.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientRegistryEvents {

    @SubscribeEvent
    public static void registerSelectItemModelProperties(RegisterSelectItemModelPropertyEvent event) {
        event.register(ResourceLocation.fromNamespaceAndPath(DimensionDelvers.MODID, "runegem_shape"), SelectRuneGemShape.TYPE);
    }

    @SubscribeEvent
    public static void registerClientTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(ImageTooltipRenderer.ImageComponent.class, ImageTooltipRenderer::new);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                ModEntities.SIMPLE_EFFECT_PROJECTILE.get(),
                SimpleEffectProjectileRenderer::new
        );
    }
}
