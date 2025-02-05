package com.dimensiondelvers.dimensiondelvers.item.runegem;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

public class Runegem extends Item {
    private final TagKey<Enchantment> tagKey;

    public Runegem(Properties properties, TagKey<Enchantment> tagKey) {
        super(properties);
        this.tagKey = tagKey;
    }

    public TagKey<Enchantment> getTagKey() {
        return tagKey;
    }
}
