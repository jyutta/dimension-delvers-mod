package com.wanderersoftherift.wotr.abilities.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.abilities.Targeting.AbstractTargeting;
import com.wanderersoftherift.wotr.abilities.effects.util.ParticleInfo;
import com.wanderersoftherift.wotr.entity.projectile.SimpleEffectProjectile;
import com.wanderersoftherift.wotr.entity.projectile.SimpleProjectileConfig;
import com.wanderersoftherift.wotr.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Optional;

public class SimpleProjectileEffect extends AbstractEffect {
    private SimpleProjectileConfig config;

    public static final MapCodec<SimpleProjectileEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            AbstractEffect.commonFields(instance).and(
                    SimpleProjectileConfig.CODEC.fieldOf("config").forGetter(SimpleProjectileEffect::getConfig)
            ).apply(instance, SimpleProjectileEffect::new)
    );

    @Override
    public MapCodec<? extends AbstractEffect> getCodec() {
        return CODEC;
    }

    public SimpleProjectileConfig getConfig() {
        return config;
    }

    public SimpleProjectileEffect(AbstractTargeting targeting, List<AbstractEffect> effects, Optional<ParticleInfo> particles, SimpleProjectileConfig config) {
        super(targeting, effects, particles);
        this.config = config;
    }

    @Override
    public void apply(Entity user, List<BlockPos> blocks, LivingEntity caster) {
        List<BlockPos> targets = getTargeting().getBlocks(user);
        applyParticlesToUser(user);
        if (!targets.isEmpty()) {
            BlockPos random = targets.get(caster.getRandom().nextIntBetweenInclusive(0, targets.size() - 1));
            EntityType<?> type = ModEntities.SIMPLE_EFFECT_PROJECTILE.get();
            Entity simpleProjectile = type.create((ServerLevel) caster.level(), null, user.getOnPos(), EntitySpawnReason.MOB_SUMMONED, false, false);
            if (simpleProjectile != null) {

                simpleProjectile.setPos(user.getEyePosition());
                if (simpleProjectile instanceof SimpleEffectProjectile projectileEntity) {
                    projectileEntity.setOwner(caster);
                    projectileEntity.setEffect(this);
                    projectileEntity.configure(config);

                    projectileEntity.shootFromRotation(user, user.getXRot(), user.getYRot(), 0, config.velocity(), 0);

                }

                caster.level().addFreshEntity(simpleProjectile);
            }
        }
    }

    public void applyDelayed(Entity target, List<BlockPos> blocks, LivingEntity caster) {
        applyParticlesToTarget(target);
        super.apply(target, blocks, caster);
    }

}
