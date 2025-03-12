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

public class BlankEffect extends AbstractEffect{

    //TODO setup healing amount as part of the codec
    public static final MapCodec<BlankEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    AbstractTargeting.DIRECT_CODEC.fieldOf("targeting").forGetter(AbstractEffect::getTargeting),
                    Codec.list(AbstractEffect.DIRECT_CODEC).fieldOf("effects").forGetter(AbstractEffect::getEffects),
                    Codec.optionalField("particles", ParticleInfo.CODEC.codec(), true).forGetter(AbstractEffect::getParticles)
            ).apply(instance, BlankEffect::new)
    );

    @Override
    public MapCodec<? extends AbstractEffect> getCodec() {
        return CODEC;
    }

    public BlankEffect(AbstractTargeting targeting, List<AbstractEffect> effects, Optional<ParticleInfo> particles) {
        super(targeting, effects, particles);
    }

    @Override
    public void apply(Entity user, List<BlockPos> blocks, LivingEntity caster) {
        List<Entity> targets = getTargeting().getTargets(user, blocks, caster);
        applyParticlesToUser(user);

        for(Entity target: targets) {
            applyParticlesToTarget(target);
            //Then apply children affects to targets
            super.apply(target, getTargeting().getBlocks(user), caster);
        }


        if(targets.isEmpty())
        {
            super.apply(null, getTargeting().getBlocks(user), caster);
        }
    }
}
