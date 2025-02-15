package com.dimensiondelvers.dimensiondelvers.init;


import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.modifier.effect.AbstractModifierEffect;
import com.dimensiondelvers.dimensiondelvers.modifier.effect.AttributeModifierEffect;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import java.util.function.Supplier;


public class ModAbstractModifiers {

    // Registry and key for Abstract Modifiers
    public static final ResourceKey<Registry<MapCodec<? extends AbstractModifierEffect>>> MODIFIER_TYPE_KEY = ResourceKey.createRegistryKey(DimensionDelvers.id("modifier_type"));

    public static final ResourceKey<Registry<AbstractModifierEffect>> MODIFIER_KEY = ResourceKey.createRegistryKey(DimensionDelvers.id("modifier"));

    public static final Registry<MapCodec<? extends AbstractModifierEffect>> MODIFIER_TYPE_REGISTRY = new RegistryBuilder<>(MODIFIER_TYPE_KEY).create();

    // Creating the Deferred Register
    public static final DeferredRegister<MapCodec<? extends AbstractModifierEffect>> MODIFIER_TYPES = DeferredRegister.create(
            MODIFIER_TYPE_KEY, DimensionDelvers.MODID
    );

    public static final Supplier<MapCodec<? extends AbstractModifierEffect>> GEAR_MODIFIER = MODIFIER_TYPES.register(
            "gear_modifier", () -> AttributeModifierEffect.MODIFIER_CODEC
    );
}
