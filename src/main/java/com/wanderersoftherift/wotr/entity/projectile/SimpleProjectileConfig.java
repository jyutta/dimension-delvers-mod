package com.wanderersoftherift.wotr.entity.projectile;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record SimpleProjectileConfig(ResourceLocation texture, float velocity, boolean gravityAffected, float gravity) {
    public static final MapCodec<SimpleProjectileConfig> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("texture").forGetter(SimpleProjectileConfig::texture),
                    Codec.FLOAT.fieldOf("velocity").forGetter(SimpleProjectileConfig::velocity),
                    Codec.BOOL.optionalFieldOf("gravity_affected", true).forGetter(SimpleProjectileConfig::gravityAffected),
                    Codec.FLOAT.optionalFieldOf("gravity", 0.05F).forGetter(SimpleProjectileConfig::gravity)
            ).apply(instance, SimpleProjectileConfig::new)
    );
}
