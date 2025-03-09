package com.dimensiondelvers.dimensiondelvers.abilities.effects;

import com.dimensiondelvers.dimensiondelvers.abilities.Targeting.AbstractTargeting;
import com.dimensiondelvers.dimensiondelvers.abilities.effects.util.ParticleInfo;
import com.dimensiondelvers.dimensiondelvers.entity.projectile.SimpleEffectProjectile;
import com.dimensiondelvers.dimensiondelvers.init.ModEntities;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class SimpleProjectileEffect extends AbstractEffect {
    ResourceLocation texture;
    Vec3 velocity;

    /*
     * For now just handle any projectile given, but we will look into handling a dynamic projectile that can handle effects attached to it
     */

    public static final MapCodec<SimpleProjectileEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    AbstractTargeting.DIRECT_CODEC.fieldOf("targeting").forGetter(AbstractEffect::getTargeting),
                    Codec.list(AbstractEffect.DIRECT_CODEC).fieldOf("effects").forGetter(AbstractEffect::getEffects),
                    Codec.optionalField("particles", ParticleInfo.CODEC.codec(), true).forGetter(AbstractEffect::getParticles),
                    ResourceLocation.CODEC.fieldOf("texture").forGetter(SimpleProjectileEffect::getTexture),
                    Vec3.CODEC.fieldOf("velocity").forGetter(SimpleProjectileEffect::getVelocity)
            ).apply(instance, SimpleProjectileEffect::new)
    );

    public Vec3 getVelocity() {
        return this.velocity;
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }

    @Override
    public MapCodec<? extends AbstractEffect> getCodec() {
        return CODEC;
    }

    public SimpleProjectileEffect(AbstractTargeting targeting, List<AbstractEffect> effects, Optional<ParticleInfo> particles, ResourceLocation texture, Vec3 velocity) {
        super(targeting, effects, particles);
        this.texture = texture;
        this.velocity = velocity;
    }

    @Override
    public void apply(Entity user, List<BlockPos> blocks, Player caster) {
        List<BlockPos> targets = getTargeting().getBlocks(user);
        applyParticlesToUser(user);
        if (!targets.isEmpty()) {
            BlockPos random = targets.get(caster.getRandom().nextIntBetweenInclusive(0, targets.size() - 1));
            EntityType<?> type = ModEntities.SIMPLE_EFFECT_PROJECTILE.get();
            Entity summon = type.create((ServerLevel) caster.level(), null, user.getOnPos(), EntitySpawnReason.MOB_SUMMONED, false, false);
            if (summon != null) {

                summon.setPos(user.getEyePosition());
                if (summon instanceof SimpleEffectProjectile projectileEntity) {
                    projectileEntity.setOwner(caster);
                    projectileEntity.setEffect(this);

                    projectileEntity.shootFromRotation(user, (float) (user.getXRot() + velocity.y), (float) (user.getYRot() + velocity.x), 0, 1, 0);

                }

                caster.level().addFreshEntity(summon);
                applyParticlesToTarget(summon);
                super.apply(summon, getTargeting().getBlocks(user), caster);
            }
        }
    }

    public void applyDelayed(Entity target, List<BlockPos> blocks, Player caster) {
        applyParticlesToTarget(target);
        super.apply(target, blocks, caster);
    }

}
