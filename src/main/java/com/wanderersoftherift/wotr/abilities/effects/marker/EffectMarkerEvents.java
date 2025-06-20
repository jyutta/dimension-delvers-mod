package com.wanderersoftherift.wotr.abilities.effects.marker;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.init.ModAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

/**
 * Updated effect markers
 */
@EventBusSubscriber(modid = WanderersOfTheRift.MODID, bus = EventBusSubscriber.Bus.GAME)
public class EffectMarkerEvents {
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && !Minecraft.getInstance().isPaused()) {
            EffectDisplayData data = player.getData(ModAttachments.EFFECT_DISPLAY);
            data.tick();
        }
    }
}
