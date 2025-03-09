package com.dimensiondelvers.dimensiondelvers.init;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.entity.projectile.SimpleEffectProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {

    public static final DeferredRegister.Entities ENTITIES = DeferredRegister.createEntities(DimensionDelvers.MODID);

    public static DeferredHolder<EntityType<?>, EntityType<SimpleEffectProjectile>> SIMPLE_EFFECT_PROJECTILE = ENTITIES.registerEntityType("simple_effect_projectile",
            SimpleEffectProjectile::new,
            MobCategory.MISC);

}
