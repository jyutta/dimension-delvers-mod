package com.wanderersoftherift.wotr.init;

import com.mojang.serialization.MapCodec;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.core.inventory.snapshot.RetainInventorySnapshotIdLootModifier;
import com.wanderersoftherift.wotr.loot.lootmodifiers.RemoveEnchantedItemsModifierAndAddSockets;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_SERIALIZERS = DeferredRegister
            .create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, WanderersOfTheRift.MODID);

    public static final Supplier<MapCodec<RetainInventorySnapshotIdLootModifier>> RETAIN_INVENTORY_SNAPSHOT_ID = GLOBAL_LOOT_MODIFIER_SERIALIZERS
            .register("retain_inventory_snapshot_id", () -> RetainInventorySnapshotIdLootModifier.CODEC);

    public static final Supplier<MapCodec<RemoveEnchantedItemsModifierAndAddSockets>> REMOVE_ENCHANTED_ITEMS = GLOBAL_LOOT_MODIFIER_SERIALIZERS
            .register("remove_enchanted_items_and_add_sockets", () -> RemoveEnchantedItemsModifierAndAddSockets.CODEC);
}
