package com.wanderersoftherift.wotr.item.ability;

import com.wanderersoftherift.wotr.abilities.AbstractAbility;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;

public record AbilityItemData(Holder<AbstractAbility> ability) {
    public static Codec<AbilityItemData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            AbstractAbility.CODEC.fieldOf("ability").forGetter(AbilityItemData::ability)
    ).apply(inst, AbilityItemData::new));
}
