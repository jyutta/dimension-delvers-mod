package com.wanderersoftherift.wotr.rift.objective;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.core.rift.RiftEvent;
import com.wanderersoftherift.wotr.init.RegistryEvents;
import com.wanderersoftherift.wotr.network.S2CRiftObjectiveStatusPacket;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;

@EventBusSubscriber(modid = WanderersOfTheRift.MODID)
public class RiftObjectiveEvents {

    @SubscribeEvent
    public static void onPlayerJoinLevel(EntityJoinLevelEvent event) {
        if (event.getEntity().level() instanceof ServerLevel serverLevel
                && event.getEntity() instanceof ServerPlayer player) {
            LevelRiftObjectiveData data = LevelRiftObjectiveData.getFromLevel(serverLevel);
            if (data.getObjective() != null) {
                PacketDistributor.sendToPlayer(player,
                        new S2CRiftObjectiveStatusPacket(Optional.of(data.getObjective())));
                Component objectiveStartMessage = data.getObjective().getObjectiveStartMessage();
                if (objectiveStartMessage != null) {
                    player.displayClientMessage(objectiveStartMessage, false);
                }
            } else {
                PacketDistributor.sendToPlayer(player, new S2CRiftObjectiveStatusPacket(Optional.empty()));

            }
        }
    }

    @SubscribeEvent
    public static void onRiftOpened(RiftEvent.Created event) {
        Holder<ObjectiveType> objectiveType = event.getConfig()
                .objective()
                .orElseGet(() -> event.getLevel()
                        .registryAccess()
                        .lookupOrThrow(RegistryEvents.OBJECTIVE_REGISTRY)
                        .getRandom(event.getLevel().getRandom())
                        .orElseThrow(() -> new IllegalStateException("No objectives available")));

        OngoingObjective objective = objectiveType.value().generate(event.getLevel());
        LevelRiftObjectiveData data = LevelRiftObjectiveData.getFromLevel(event.getLevel());
        data.setObjective(objective);
    }
}
