package com.wanderersoftherift.wotr.abilities.effects;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.abilities.Targeting.AbstractTargeting;
import com.wanderersoftherift.wotr.abilities.effects.util.ParticleInfo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TargetEffect extends AbstractEffect {

    public static final MapCodec<TargetEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    AbstractTargeting.DIRECT_CODEC.fieldOf("targeting").forGetter(AbstractEffect::getTargeting),
                    Codec.list(AbstractEffect.DIRECT_CODEC).fieldOf("effects").forGetter(AbstractEffect::getEffects),
                    Codec.optionalField("particles", ParticleInfo.CODEC.codec(), true).forGetter(AbstractEffect::getParticles)
            ).apply(instance, TargetEffect::new)
    );

    @Override
    public MapCodec<? extends AbstractEffect> getCodec() {
        return CODEC;
    }

    public TargetEffect(AbstractTargeting targeting, List<AbstractEffect> effects, Optional<ParticleInfo> particles) {
        super(targeting, effects, particles);
    }

    @Override
    public void apply(Entity user, List<BlockPos> blocks, Player caster) {
        List<Entity> targets = getTargeting().getTargets(user, blocks, caster);
        applyParticlesToUser(user);

        WanderersOfTheRift.LOGGER.info("Targetting: " + targets.size());
        for(Entity target: targets) {
            applyParticlesToTarget(target);
            //Then apply children effects to targets
            super.apply(target, getTargeting().getBlocks(user), caster);
        }


        if(targets.isEmpty())
        {
            super.apply(null, getTargeting().getBlocks(user), caster);
        }
    }

    @Override
    public Set<Holder<Attribute>> getApplicableAttributes() {
        return super.getApplicableAttributes();
    }
}
