package com.dimensiondelvers.dimensiondelvers.init;


import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.modifier.AbstractModifier;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;


public class ModAbstractModifiers {

    // Registry and key for Abstract Modifiers
    public static final ResourceKey<Registry<MapCodec<? extends AbstractModifier>>>ABSTRACT_MODIFIER_KEY = ResourceKey.createRegistryKey(DimensionDelvers.id("abstract_modifier"));

    public static final ResourceKey<Registry<AbstractModifier>> MODIFIER_KEY = ResourceKey.createRegistryKey(DimensionDelvers.id("modifier"));

    public static final Registry<MapCodec<? extends AbstractModifier>> ABSTRACT_MODIFIER_REGISTRY = new RegistryBuilder<>(ABSTRACT_MODIFIER_KEY).create();

    // Creating the Deferred Register
    public static final DeferredRegister<MapCodec<? extends AbstractModifier>> ABSTRACT_MODIFIER = DeferredRegister.create(
            ABSTRACT_MODIFIER_KEY, DimensionDelvers.MODID
    );
}
