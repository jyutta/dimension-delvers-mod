package com.dimensiondelvers.dimensiondelvers.item.socket;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

// Vanilla Equivalent ItemEnchantments
public record GearSocket(String type, Holder<Enchantment> modifier, Holder<Item> runegem) {
    // Further define type
    // Needs to have a modifier and a Runegem
    // should eventually also have the roll of the modifier (and a tier?)
    public static Codec<GearSocket> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.STRING.fieldOf("type").forGetter(GearSocket::type),
            Enchantment.CODEC.optionalFieldOf("modifier", null).forGetter(GearSocket::modifier),
            RegistryFixedCodec.create(Registries.ITEM).optionalFieldOf("modifier", null).forGetter(GearSocket::runegem)
    ).apply(inst, GearSocket::new));
}