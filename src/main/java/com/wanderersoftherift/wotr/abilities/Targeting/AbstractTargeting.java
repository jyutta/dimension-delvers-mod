package com.wanderersoftherift.wotr.abilities.Targeting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.wanderersoftherift.wotr.Registries.AbilityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractTargeting {
    public abstract MapCodec<? extends AbstractTargeting> getCodec();
    public static final Codec<AbstractTargeting> DIRECT_CODEC = AbilityRegistry.EFFECT_TARGETING_REGISTRY.byNameCodec().dispatch(AbstractTargeting::getCodec, Function.identity());

    /**
     * @param currentEntity This is the entity which is using the effect, this can be any entity down a chain based on the effect list, this determines the location around where the effect is targeting
     * @param blocks A list of blocks which can be a point of reference for targeting enemies around them. This is mainly used for raycasting based effects
     * @param caster The original player starting the effect chain
     * @return The list of entities selected by the targeting method.
     */
    public List<Entity> getTargets(Entity currentEntity, List<BlockPos> blocks, LivingEntity caster) {
        List<Entity> targets = new ArrayList<>();
        if(currentEntity != null) {
            targets.addAll(getTargetsFromEntity(currentEntity, caster));
        }
        else {
            targets.addAll(getTargetsFromBlocks(blocks, caster));
        }

        return targets;
    }

    protected List<Entity> getTargetsFromEntity(Entity entity, LivingEntity caster) {
        return new ArrayList<>();
    }

    protected List<Entity> getTargetsFromBlocks(List<BlockPos> blocks, LivingEntity caster) {
        return new ArrayList<>();
    }

    public List<BlockPos> getBlocks(Entity user) {
        return new ArrayList<>();
    }

    public List<BlockPos> getBlocksInArea(LivingEntity caster, Entity entity, List<BlockPos> targetPos) {
        return new ArrayList<>();
    }
}
