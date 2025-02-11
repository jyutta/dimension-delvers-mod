package com.dimensiondelvers.dimensiondelvers.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class Modifier extends AbstractModifier {

    private final String attribute;
    private final int minRoll;
    private final int maxRoll;

    public Modifier(String attribute, int minRoll, int maxRoll) {

        this.attribute = attribute;
        this.minRoll = minRoll;
        this.maxRoll = maxRoll;
    }
    @Override
    public MapCodec<? extends AbstractModifier> getCodec() {
        return MODIFIER_CODEC;
    }

    public static final MapCodec<Modifier> MODIFIER_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("attribute").forGetter(Modifier::getAttribute),
            Codec.INT.fieldOf("minRoll").forGetter(Modifier::getMinimumRoll),
            Codec.INT.fieldOf("maxRoll").forGetter(Modifier::getMaximumRoll)
            ).apply(instance, Modifier::new)
    );

    @Override
    public void applyModifier() {

    }

    public String getAttribute(){
        return attribute;
    }

    public int getMinimumRoll(){
        return minRoll;
    }

    public int getMaximumRoll(){
        return maxRoll;
    }
}
