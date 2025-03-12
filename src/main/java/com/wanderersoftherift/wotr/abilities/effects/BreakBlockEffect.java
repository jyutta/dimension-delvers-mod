package com.wanderersoftherift.wotr.abilities.effects;

import com.wanderersoftherift.wotr.abilities.Targeting.AbstractTargeting;
import com.wanderersoftherift.wotr.abilities.effects.util.ParticleInfo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Optional;

public class BreakBlockEffect extends AbstractEffect{

    public BreakBlockEffect(AbstractTargeting targeting, List<AbstractEffect> effects, Optional<ParticleInfo> particles) {
        super(targeting, effects, particles);
    }

    public static final MapCodec<BreakBlockEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    AbstractTargeting.DIRECT_CODEC.fieldOf("targeting").forGetter(AbstractEffect::getTargeting),
                    Codec.list(AbstractEffect.DIRECT_CODEC).fieldOf("effects").forGetter(AbstractEffect::getEffects),
                    Codec.optionalField("particles", ParticleInfo.CODEC.codec(), true).forGetter(AbstractEffect::getParticles)
            ).apply(instance, BreakBlockEffect::new)
    );

    @Override
    public MapCodec<? extends AbstractEffect> getCodec() {
        return CODEC;
    }

    @Override
    public void apply(Entity user, List<BlockPos> blocks, LivingEntity caster) {
        List<Entity> targets = getTargeting().getTargets(user, blocks, caster);
        applyParticlesToUser(user);

        List<BlockPos> areaBlocks = getTargeting().getBlocksInArea(caster, user, blocks);

        for(BlockPos pos: areaBlocks)
        {
            //TODO: Make fortune work maybe? (Apply tool enchants etc)
           if(caster.level().getBlockState(pos).canEntityDestroy(caster.level(), pos, caster) && caster.level().getBlockState(pos).getBlock().defaultDestroyTime() > -1) {
               caster.level().destroyBlock(pos, true, caster);
           }
        }

        if(targets.isEmpty())
        {
            super.apply(null, getTargeting().getBlocks(user), caster);
        }
    }
}
