package com.dimensiondelvers.dimensiondelvers.abilities.effects;

import com.dimensiondelvers.dimensiondelvers.abilities.Targetting.EffectTargeting;
import com.dimensiondelvers.dimensiondelvers.abilities.effects.util.ParticleInfo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Optional;

public class DamageEffect extends AbstractEffect{
    private float damageAmount = 0;
    private Holder<DamageType> damageTypeKey;
    public DamageEffect(EffectTargeting targeting, List<AbstractEffect> effects, Optional<ParticleInfo> particles, float amount, Holder<DamageType> damageTypeKey) {
        super(targeting, effects, particles);
        this.damageAmount = amount;
        this.damageTypeKey = damageTypeKey;
    }

    public static final MapCodec<DamageEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    EffectTargeting.CODEC.fieldOf("targeting").forGetter(AbstractEffect::getTargeting),
                    Codec.list(AbstractEffect.DIRECT_CODEC).fieldOf("effects").forGetter(AbstractEffect::getEffects),
                    Codec.optionalField("particles", ParticleInfo.CODEC.codec(), true).forGetter(AbstractEffect::getParticles),
                    Codec.FLOAT.fieldOf("amount").forGetter(DamageEffect::getAmount),
                    DamageType.CODEC.fieldOf("damage_type").forGetter(DamageEffect::getDamageTypeKey)
            ).apply(instance, DamageEffect::new)
    );

    private Holder<DamageType> getDamageTypeKey() {
        return damageTypeKey;
    }

    private float getAmount() {
        return damageAmount;
    }

    @Override
    public MapCodec<? extends AbstractEffect> getCodec() {
        return CODEC;
    }

    @Override
    public void apply(Entity user, List<BlockPos> blocks, Player caster) {
        List<Entity> targets = getTargeting().getTargets(user, blocks, caster);
        DamageSource damageSource = new DamageSource(
                caster.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(this.damageTypeKey.getKey()),
                null,
                caster,
                null
        );

        applyParticlesToUser(user);

        for(Entity target: targets) {
            applyParticlesToTarget(target);
            if(target instanceof LivingEntity livingTarget)
            {
                livingTarget.hurtServer((ServerLevel) user.level(), damageSource, damageAmount);
            }
            //Then apply children affects to targets
            super.apply(target, getTargeting().getBlocks(user), caster);
        }

        if(targets.isEmpty())
        {
            super.apply(null, getTargeting().getBlocks(user), caster);
        }
    }
}
