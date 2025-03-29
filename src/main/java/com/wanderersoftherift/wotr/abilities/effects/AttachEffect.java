package com.wanderersoftherift.wotr.abilities.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.abilities.Targeting.AbstractTargeting;
import com.wanderersoftherift.wotr.abilities.effects.marker.EffectMarker;
import com.wanderersoftherift.wotr.abilities.effects.util.ParticleInfo;
import com.wanderersoftherift.wotr.init.ModAttachments;
import com.wanderersoftherift.wotr.init.RegistryEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Optional;

/**
 * AttachEffect attaches all of its child effects to each target entity, with a durationTicks
 */
public class AttachEffect extends AbstractEffect {

    public static final MapCodec<AttachEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> AbstractEffect
            .commonFields(instance)
            .and(Codec.INT.fieldOf("ticks").forGetter(AttachEffect::getTicks))
            .and(RegistryFixedCodec.create(RegistryEvents.EFFECT_MARKER_REGISTRY).optionalFieldOf("display").forGetter(x -> Optional.ofNullable(x.getDisplay())))
            .apply(instance, AttachEffect::new));

    private int ticks;
    private Holder<EffectMarker> display;

    public AttachEffect(AbstractTargeting targeting, List<AbstractEffect> effects, Optional<ParticleInfo> particles, int ticks, Optional<Holder<EffectMarker>> display) {
        this(targeting, effects, particles, ticks, display.orElse(null));
    }

    public AttachEffect(AbstractTargeting targeting, List<AbstractEffect> effects, Optional<ParticleInfo> particles, int ticks, Holder<EffectMarker> display) {
        super(targeting, effects, particles);
        this.ticks = ticks;
        this.display = display;
    }

    @Override
    public void apply(Entity user, List<BlockPos> blocks, LivingEntity caster) {
        List<Entity> targets = getTargeting().getTargets(user, blocks, caster);

        applyParticlesToUser(user);

        for (Entity target : targets) {
            applyParticlesToTarget(target);

            // I hope this changes
            if (target instanceof LivingEntity livingTarget) {
                target.getData(ModAttachments.ATTACHED_EFFECTS).attach(livingTarget, getEffects(), caster, ticks, display);
            }

            super.apply(target, getTargeting().getBlocks(user), caster);
        }

        if (targets.isEmpty()) {
            super.apply(null, getTargeting().getBlocks(user), caster);
        }
    }

    @Override
    public MapCodec<? extends AbstractEffect> getCodec() {
        return CODEC;
    }

    public int getTicks() {
        return ticks;
    }

    public Holder<EffectMarker> getDisplay() {
        return display;
    }
}
