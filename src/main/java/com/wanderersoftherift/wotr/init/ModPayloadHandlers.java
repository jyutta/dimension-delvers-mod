package com.wanderersoftherift.wotr.init;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.item.skillgem.AbilitySlots;
import com.wanderersoftherift.wotr.network.AbilitySlotsContentPayload;
import com.wanderersoftherift.wotr.network.AbilitySlotsCooldownsPayload;
import com.wanderersoftherift.wotr.network.AbilitySlotsUpdatePayload;
import com.wanderersoftherift.wotr.network.SelectAbilitySlotPayload;
import com.wanderersoftherift.wotr.network.SelectSkillUpgradePayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = WanderersOfTheRift.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ModPayloadHandlers {
    public static final String PROTOCOL_VERSION = "0.0.1";

    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(WanderersOfTheRift.MODID).versioned(PROTOCOL_VERSION);

        registrar.playToServer(SelectSkillUpgradePayload.ID, SelectSkillUpgradePayload.STREAM_CODEC, SelectSkillUpgradePayload::handleOnServer);
        registrar.playToServer(SelectAbilitySlotPayload.ID, SelectAbilitySlotPayload.STREAM_CODEC, SelectAbilitySlotPayload::handleOnServer);
        registrar.playToClient(AbilitySlotsContentPayload.ID, AbilitySlotsContentPayload.STREAM_CODEC, AbilitySlotsContentPayload::handleOnClient);
        registrar.playToClient(AbilitySlotsUpdatePayload.ID, AbilitySlotsUpdatePayload.STREAM_CODEC, AbilitySlotsUpdatePayload::handleOnClient);
        registrar.playToClient(AbilitySlotsCooldownsPayload.ID, AbilitySlotsCooldownsPayload.STREAM_CODEC, AbilitySlotsCooldownsPayload::handleOnClient);
    }

    @SubscribeEvent
    public static void onPlayerJoinedEvent(PlayerEvent.PlayerLoggedInEvent event) {
        replicateAbilities(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerSpawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        replicateAbilities(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerSpawnEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
        replicateAbilities(event.getEntity());
    }

    private static void replicateAbilities(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        AbilitySlots abilitySlots = serverPlayer.getData(ModAttachments.ABILITY_SLOTS);
        PacketDistributor.sendToPlayer(serverPlayer, new AbilitySlotsContentPayload(abilitySlots.getAbilities(), abilitySlots.getSelectedSlot()));
        PacketDistributor.sendToPlayer(serverPlayer, new AbilitySlotsCooldownsPayload(player.getData(ModAttachments.ABILITY_COOLDOWNS)));
    }

}
