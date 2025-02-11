package com.dimensiondelvers.dimensiondelvers.modifier;

import com.mojang.serialization.*;
import java.util.function.Function;
import static com.dimensiondelvers.dimensiondelvers.init.ModAbstractModifiers.ABSTRACT_MODIFIER_REGISTRY;



public abstract class AbstractModifier {
    public abstract MapCodec<? extends AbstractModifier> getCodec();

    public abstract void applyModifier();

    public static final Codec<AbstractModifier> DIRECT_CODEC = ABSTRACT_MODIFIER_REGISTRY.byNameCodec().dispatch(AbstractModifier::getCodec, Function.identity());
}
