package com.wanderersoftherift.wotr.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.core.rift.RiftData;
import com.wanderersoftherift.wotr.init.ModDataComponentType;
import com.wanderersoftherift.wotr.item.implicit.GearImplicits;
import com.wanderersoftherift.wotr.item.socket.GearSockets;
import com.wanderersoftherift.wotr.util.ItemTagUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.wanderersoftherift.wotr.init.ModLootItemFunctionTypes.ROLL_GEAR_FUNCTION;

public class RollGearFunction extends LootItemConditionalFunction {
    public static final MapCodec<RollGearFunction> CODEC = RecordCodecBuilder.mapCodec(
            inst -> commonFields(inst).and(Codec.INT.fieldOf("min_sockets").forGetter(RollGearFunction::getMinSockets))
                    .and(Codec.INT.fieldOf("max_sockets").forGetter(RollGearFunction::getMaxSockets))
                    .and(Codec.STRING.fieldOf("tag_location").forGetter(RollGearFunction::getTagLocation))

                    .apply(inst, RollGearFunction::new));

    private final int minSockets;
    private final int maxSockets;
    private final String tagLocation;

    protected RollGearFunction(List<LootItemCondition> predicates, int minSockets, int maxSockets, String tagLocation) {
        super(predicates);
        this.minSockets = minSockets;
        this.maxSockets = maxSockets;
        this.tagLocation = tagLocation;
    }

    public int getMinSockets() {
        return minSockets;
    }

    public int getMaxSockets() {
        return maxSockets;
    }

    public String getTagLocation() {
        return tagLocation;
    }

    @Override
    public LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
        return ROLL_GEAR_FUNCTION.get();
    }

    @Override
    protected ItemStack run(ItemStack itemStack, LootContext lootContext) {
        return generateItemStack(itemStack, lootContext.getLevel(), lootContext.getRandom());
    }

    private @NotNull ItemStack generateItemStack(ItemStack itemStack, ServerLevel serverLevel, RandomSource random) {

        itemStack = ItemTagUtil.getRandomItemStackFromTag(itemStack, tagLocation, random);
        // todo add rift tier to ModDataComponentType for gear should it have its own place in implicits or does it need
        // to be its own ModDataComponentType?
        itemStack.set(ModDataComponentType.GEAR_SOCKETS, GearSockets.randomSockets(minSockets, maxSockets, random));
        GearImplicits implicits = itemStack.get(ModDataComponentType.GEAR_IMPLICITS);
        if (implicits != null) {
            implicits.modifierInstances(itemStack, serverLevel);
        }
        RiftData riftData = RiftData.get(serverLevel);
        if (riftData.getTier() > 0) {
            itemStack.set(ModDataComponentType.ITEM_RIFT_TIER, riftData.getTier());
        }
        return itemStack;
    }

    public static Builder<?> rollRiftGear(int minSockets, int maxSockets, String tagLocation) {
        return simpleBuilder(
                (lootItemConditions) -> new RollGearFunction(lootItemConditions, minSockets, maxSockets, tagLocation));
    }
}
