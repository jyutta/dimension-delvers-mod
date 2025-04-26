package com.wanderersoftherift.wotr.rift.objective;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.core.rift.RiftEvent;
import com.wanderersoftherift.wotr.core.rift.RiftLevelManager;
import com.wanderersoftherift.wotr.gui.menu.RiftCompleteMenu;
import com.wanderersoftherift.wotr.init.ModAttachments;
import com.wanderersoftherift.wotr.init.RegistryEvents;
import com.wanderersoftherift.wotr.network.S2CRiftObjectiveStatusPacket;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;

/**
 * Event subscriber for objective handling
 */
@EventBusSubscriber(modid = WanderersOfTheRift.MODID)
public class RiftObjectiveEvents {
    private static final ResourceKey<LootTable> SUCCESS_TABLE = ResourceKey.create(Registries.LOOT_TABLE,
            WanderersOfTheRift.id("rift_objective/success"));
    private static final ResourceKey<LootTable> FAIL_TABLE = ResourceKey.create(Registries.LOOT_TABLE,
            WanderersOfTheRift.id("rift_objective/fail"));
    private static final ResourceKey<LootTable> SURVIVE_TABLE = ResourceKey.create(Registries.LOOT_TABLE,
            WanderersOfTheRift.id("rift_objective/survive"));

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
    public static void onPlayerDiedInRift(RiftEvent.PlayerDied event) {
        event.getPlayer().setData(ModAttachments.DIED_IN_RIFT, true);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        boolean diedInRift = event.getEntity().getData(ModAttachments.DIED_IN_RIFT);
        if (diedInRift && event.getEntity() instanceof ServerPlayer player) {
            event.getEntity()
                    .openMenu(new SimpleMenuProvider(
                            (containerId, playerInventory, p) -> new RiftCompleteMenu(containerId, playerInventory,
                                    ContainerLevelAccess.create(event.getEntity().level(), p.getOnPos()),
                                    RiftCompleteMenu.FLAG_FAILED,
                                    event.getEntity()
                                            .getData(ModAttachments.PRE_RIFT_STATS)
                                            .getCustomStatDelta(player)),
                            Component.translatable(WanderersOfTheRift.translationId("container", "rift_complete"))));
            event.getEntity().setData(ModAttachments.DIED_IN_RIFT, false);
            if (event.getEntity().containerMenu instanceof RiftCompleteMenu menu) {

                generateObjectiveLoot(menu, player, FAIL_TABLE);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLeaveLevel(PlayerEvent.PlayerChangedDimensionEvent event) {
        ServerLevel riftLevel = RiftLevelManager.getRiftLevel(event.getFrom().location());

        if (riftLevel != null && event.getEntity() instanceof ServerPlayer player) {
            OngoingObjective objective = LevelRiftObjectiveData.getFromLevel(riftLevel).getObjective();
            boolean success = objective != null && objective.isComplete();
            event.getEntity()
                    .openMenu(new SimpleMenuProvider(
                            (containerId, playerInventory, p) -> new RiftCompleteMenu(containerId, playerInventory,
                                    ContainerLevelAccess.create(event.getEntity().level(), p.getOnPos()),
                                    success ? RiftCompleteMenu.FLAG_SUCCESS : RiftCompleteMenu.FLAG_SURVIVED,
                                    event.getEntity()
                                            .getData(ModAttachments.PRE_RIFT_STATS)
                                            .getCustomStatDelta(player)),
                            Component.translatable(WanderersOfTheRift.translationId("container", "rift_complete"))));

            if (event.getEntity().containerMenu instanceof RiftCompleteMenu menu) {
                generateObjectiveLoot(menu, player, success ? SUCCESS_TABLE : SURVIVE_TABLE);
            }
        }
    }

    private static void generateObjectiveLoot(
            RiftCompleteMenu menu,
            ServerPlayer player,
            ResourceKey<LootTable> table) {
        ServerLevel level = player.serverLevel();
        LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(table);
        LootParams lootParams = new LootParams.Builder(level).withParameter(LootContextParams.THIS_ENTITY, player)
                .create(LootContextParamSets.EMPTY);
        lootTable.getRandomItems(lootParams).forEach(item -> menu.addReward(item, player));
    }
}
