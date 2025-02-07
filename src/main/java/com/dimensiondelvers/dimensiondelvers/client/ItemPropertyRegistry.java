package com.dimensiondelvers.dimensiondelvers.client;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.init.ModDataComponentType;
import com.dimensiondelvers.dimensiondelvers.init.ModItems;
import com.dimensiondelvers.dimensiondelvers.item.runegem.RunegemData;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = DimensionDelvers.MODID, value = Dist.CLIENT)
public class ItemPropertyRegistry {
    public static final ResourceLocation RUNEGEM_PROPERTY = DimensionDelvers.id("runegem_property");


    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> { // ItemProperties#register is not threadsafe, so we need to call it on the main thread
            ItemProperties.register(
                    ModItems.RUNEGEM.get(), // The item to apply the property to.
                    RUNEGEM_PROPERTY, // The id of the property.
                    (stack, level, player, seed) -> getRuneGemModel(stack) // Need to return a float-value with the given params
            );
        });
    }


    private static float getRuneGemModel(ItemStack item) {
        RunegemData data = item.get(ModDataComponentType.RUNEGEM_DATA);

        switch (data != null ? data.shape() : null) {
            case CIRCLE -> {
                return 0.0F;
            }
            case SQUARE -> {
                return 0.1F;
            }
            case TRIANGLE -> {
                return 0.2F;
            }
            case DIAMOND -> {
                return 0.3F;
            }
            case HEART -> {
                return 0.4F;
            }
            case PENTAGON -> {
                return 0.5F;
            }
            case null -> {
                return 0.0F;
            }
        }

    }
}
