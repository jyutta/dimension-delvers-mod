package com.dimensiondelvers.dimensiondelvers.item.runegem;

import com.dimensiondelvers.dimensiondelvers.init.ModAbstractModifiers;
import com.dimensiondelvers.dimensiondelvers.modifier.effect.AbstractModifierEffect;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.tags.TagKey;

public record RunegemData(RuneGemShape shape, TagKey<AbstractModifierEffect> tag, RuneGemTier tier) {
    public static Codec<RunegemData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            RuneGemShape.CODEC.fieldOf("shape").forGetter(RunegemData::shape),
            TagKey.codec(ModAbstractModifiers.MODIFIER_KEY).fieldOf("tag").forGetter(RunegemData::tag),
            RuneGemTier.CODEC.fieldOf("tier").forGetter(RunegemData::tier)
    ).apply(inst, RunegemData::new));
}
