package com.wanderersoftherift.wotr.loot.lootmodifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.item.socket.GearSockets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import static com.wanderersoftherift.wotr.init.ModTags.Items.SOCKETABLE;

public class RemoveEnchantedItemsModifierAndAddSockets extends LootModifier {

    public static final MapCodec<RemoveEnchantedItemsModifierAndAddSockets> CODEC = RecordCodecBuilder.mapCodec(
            inst -> LootModifier.codecStart(inst).apply(inst, RemoveEnchantedItemsModifierAndAddSockets::new));

    protected RemoveEnchantedItemsModifierAndAddSockets(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(
            ObjectArrayList<ItemStack> generatedLoot,
            LootContext context) {

        // Iterate through the loot and remove enchantments from each item
        for (ItemStack itemStack : generatedLoot) {
            if (itemStack.isEnchanted()) {
                EnchantmentHelper.setEnchantments(itemStack, ItemEnchantments.EMPTY); // Remove all enchantments
            }
            if (itemStack.is(SOCKETABLE)) {
                // Add sockets to the item
                GearSockets.generateForItem(itemStack, context.getLevel(), 2, 4);
            }
        }
        return generatedLoot; // Return the modified loot
    }

    @Override
    public @NotNull MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}