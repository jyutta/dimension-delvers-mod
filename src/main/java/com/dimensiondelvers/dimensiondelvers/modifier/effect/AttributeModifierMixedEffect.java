package com.dimensiondelvers.dimensiondelvers.modifier.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class AttributeModifierMixedEffect extends AbstractModifierEffect {

    private final Attribute attribute;
    private final Attribute attributeNegative;
    // private final ObjectArrayList<Integer> rolls;
    private final int minRoll;
    private final int maxRoll;
    private final int negativeMax;

    public AttributeModifierMixedEffect(Attribute attribute, Attribute attributeNegative, int minRoll, int maxRoll, int negativeMax) {

        this.attribute = attribute;
        this.attributeNegative = attributeNegative;
        this.minRoll = minRoll;
        this.maxRoll = maxRoll;
        this.negativeMax = negativeMax;
    }

    // Still being worked on, reference modifier.ModifierInstance
    @Override
    public MapCodec<? extends AbstractModifierEffect> getCodec() {
        return MODIFIER_CODEC;
    }

    public static final MapCodec<AttributeModifierMixedEffect> MODIFIER_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ATTRIBUTE.byNameCodec().fieldOf("attribute").forGetter(AttributeModifierMixedEffect::getAttribute),
            BuiltInRegistries.ATTRIBUTE.byNameCodec().fieldOf("attribute").forGetter(AttributeModifierMixedEffect::getNegativeAttribute),
            Codec.INT.fieldOf("minRoll").forGetter(AttributeModifierMixedEffect::getMinimumRoll),
            Codec.INT.fieldOf("maxRoll").forGetter(AttributeModifierMixedEffect::getMaximumRoll),
            Codec.INT.fieldOf("negativeMax").forGetter(AttributeModifierMixedEffect::getNegativeMaxRoll)
            ).apply(instance, AttributeModifierMixedEffect::new)
    );

    @Override
    public void applyModifier() {

    }

    public Attribute getAttribute(){
        return attribute;
    }

    public int getMinimumRoll(){
        return minRoll;
    }

    public int getMaximumRoll(){
        return maxRoll;
    }

    public Attribute getNegativeAttribute() {
        return attributeNegative;
    }

    public int getNegativeMaxRoll() {
        return negativeMax;
    }
}
