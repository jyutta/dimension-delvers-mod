package com.wanderersoftherift.wotr.rift.objective;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.wanderersoftherift.wotr.init.ModObjectiveTypes;
import net.minecraft.world.level.LevelAccessor;

import java.util.function.Function;

public abstract class ObjectiveType {
    public static final Codec<ObjectiveType> DIRECT_CODEC = ModObjectiveTypes.OBJECTIVE_TYPE_REGISTRY.byNameCodec()
            .dispatch(ObjectiveType::getCodec, Function.identity());

    public abstract MapCodec<? extends ObjectiveType> getCodec();

    public abstract OngoingObjective generate(LevelAccessor level);
}
