package com.wanderersoftherift.wotr.item.skillgem;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.modifier.effect.AbstractModifierEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Upgrade {

    public static final ResourceKey<Registry<Upgrade>> UPGRADE_REGISTRY_KEY = ResourceKey.createRegistryKey(WanderersOfTheRift.id("upgrade"));

    public static final Codec<Holder<Upgrade>> REGISTRY_CODEC = RegistryFixedCodec.create(UPGRADE_REGISTRY_KEY);

    public static final Codec<Upgrade> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(Upgrade::id),
            ResourceLocation.CODEC.fieldOf("icon").forGetter(Upgrade::icon),
            Codec.INT.fieldOf("maxCount").forGetter(Upgrade::maxCount),
            AbstractModifierEffect.DIRECT_CODEC.listOf().optionalFieldOf("effects", Collections.emptyList()).forGetter(Upgrade::modifierEffects)
    ).apply(instance, Upgrade::new));

    private final ResourceLocation id;
    private final ResourceLocation icon;
    private final int maxCount;
    private final List<AbstractModifierEffect> modifierEffects;
    private final Component name;
    private final Component description;

    public Upgrade(ResourceLocation id, ResourceLocation icon, int maxCount,
                   List<AbstractModifierEffect> modifierEffects) {
        this.id = id;
        this.icon = icon;
        this.maxCount = maxCount;
        this.modifierEffects = ImmutableList.copyOf(modifierEffects);
        this.name = Component.translatable("upgrade." + id.getNamespace() + "." + id.getPath() + ".name");
        this.description = Component.translatable("upgrade." + id.getNamespace() + "." + id.getPath() + ".description");
    }

    public Component name() {
        return name;
    }

    public Component description() {
        return description;
    }

    public ResourceLocation id() {
        return id;
    }

    public ResourceLocation icon() {
        return icon;
    }

    public int maxCount() {
        return maxCount;
    }

    public List<AbstractModifierEffect> modifierEffects() {
        return modifierEffects;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Upgrade) obj;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Upgrade[" + "id='" + id + "']";
    }


}
