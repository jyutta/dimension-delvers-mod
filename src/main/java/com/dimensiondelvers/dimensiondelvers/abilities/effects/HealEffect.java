package com.dimensiondelvers.dimensiondelvers.abilities.effects;

import com.dimensiondelvers.dimensiondelvers.abilities.AbilityAttributeHelper;
import com.dimensiondelvers.dimensiondelvers.abilities.AbilityAttributes;
import com.dimensiondelvers.dimensiondelvers.abilities.Targeting.AbstractTargeting;
import com.dimensiondelvers.dimensiondelvers.abilities.effects.util.ParticleInfo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class HealEffect extends AbstractEffect {
    private float healAmount = 0;

    //TODO setup healing amount as part of the codec
    public static final MapCodec<HealEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    AbstractTargeting.DIRECT_CODEC.fieldOf("targeting").forGetter(AbstractEffect::getTargeting),
                    Codec.list(AbstractEffect.DIRECT_CODEC).fieldOf("effects").forGetter(AbstractEffect::getEffects),
                    Codec.optionalField("particles", ParticleInfo.CODEC.codec(), true).forGetter(AbstractEffect::getParticles),
                    Codec.FLOAT.fieldOf("amount").forGetter(HealEffect::getAmount)
            ).apply(instance, HealEffect::new)
    );

    public float getAmount() {
        return healAmount;
    }

    @Override
    public MapCodec<? extends AbstractEffect> getCodec() {
        return CODEC;
    }

    public HealEffect(AbstractTargeting targeting, List<AbstractEffect> effects, Optional<ParticleInfo> particles, float amount) {
        super(targeting, effects, particles);
        this.healAmount = amount;
    }

    @Override
    public void apply(Entity user, List<BlockPos> blocks, Player caster) {
        List<Entity> targets = getTargeting().getTargets(user, blocks, caster);
        applyParticlesToUser(user);

        float finalHealAmount = AbilityAttributeHelper.getAbilityAttribute(AbilityAttributes.HEAL_POWER, healAmount, caster);
        for(Entity target: targets) {
            applyParticlesToTarget(target);
            if(target instanceof LivingEntity living)
            {
                living.heal(finalHealAmount);
            }
            //Then apply children affects to targets
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
