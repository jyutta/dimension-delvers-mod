package com.wanderersoftherift.wotr.abilities.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.abilities.Targeting.AbstractTargeting;
import com.wanderersoftherift.wotr.abilities.effects.util.ParticleInfo;
import com.wanderersoftherift.wotr.entity.projectile.SimpleEffectProjectile;
import com.wanderersoftherift.wotr.entity.projectile.SimpleProjectileConfig;
import com.wanderersoftherift.wotr.init.ModAttributes;
import com.wanderersoftherift.wotr.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

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
    public void apply(Entity source, List<BlockPos> blocks, EffectContext context) {
        List<BlockPos> targets = getTargeting().getBlocks(source);
        applyParticlesToUser(source);
        if (!targets.isEmpty()) {
            EntityType<?> type = ModEntities.SIMPLE_EFFECT_PROJECTILE.get();
            int numberOfProjectiles = getNumberOfProjectiles(context);

            float spread = getSpread(context);
            float f1 = numberOfProjectiles == 1 ? 0.0F : 2.0F * spread / (float) (numberOfProjectiles - 1);
            float f2 = (float) ((numberOfProjectiles - 1) % 2) * f1 / 2.0F;
            float f3 = 1.0F;
            for (int i = 0; i < numberOfProjectiles; i++) {
                float angle = f2 + f3 * (float)((i + 1) / 2) * f1;
                f3 = -f3;
                spawnProjectile(source, type, angle, context);
            }
        }
    }

    private float getSpread(EffectContext context) {
        return context.getAbilityAttribute(ModAttributes.PROJECTILE_SPREAD, 15);
    }

    private int getNumberOfProjectiles(EffectContext context) {
        return (int) context.getAbilityAttribute(ModAttributes.PROJECTILE_COUNT, config.projectiles());
    }

    private void spawnProjectile(Entity user, EntityType<?> type, float angle, EffectContext context) {
        Entity simpleProjectile = type.create((ServerLevel) context.level(), null, user.getOnPos(), EntitySpawnReason.MOB_SUMMONED, false, false);
        if (simpleProjectile instanceof SimpleEffectProjectile projectileEntity) {
            projectileEntity.setPos(user.getEyePosition());
            projectileEntity.setOwner(context.caster());
            projectileEntity.setEffect(this);
            projectileEntity.configure(config);

            projectileEntity.shootFromRotation(user, user.getXRot(), user.getYRot() + angle, 0, context.getAbilityAttribute(ModAttributes.PROJECTILE_SPEED, config.velocity()), 0);

            context.level().addFreshEntity(simpleProjectile);
        }
    }

    public void applyDelayed(Level level, Entity target, List<BlockPos> blocks, EffectContext context) {
        context.enableModifiers();
        try {
            applyParticlesToTarget(target);
            applyParticlesToTargetBlocks(level, blocks);
            super.apply(target, blocks, context);
        } finally {
            context.disableModifiers();
        }
    }

}
