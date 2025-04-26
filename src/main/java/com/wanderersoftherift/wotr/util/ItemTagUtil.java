package com.wanderersoftherift.wotr.util;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ItemTagUtil {

    public static TagKey<Item> findTagWithPrefixOnItemStack(ItemStack itemStack, String prefix) {
        for (TagKey<Item> tag : itemStack.getTags().toList()) {
            ResourceLocation tagLocation = tag.location();
            if (tagLocation.getPath().startsWith(prefix)) {
                return tag;
            }
        }
        return null;
    }

    public static ItemStack getRandomItemStackFromTag(TagKey<Item> tag, RandomSource random) {
        // Get all items in the tag
        List<Item> itemsInTag = getItemsFromItemTag(tag);
        Item randomItem = itemsInTag.get(random.nextInt(itemsInTag.size()));
        return new ItemStack(randomItem);
    }

    public static List<Item> getItemsFromItemTag(TagKey<Item> tag) {
        return BuiltInRegistries.ITEM.get(tag).map(it -> it.stream().map(Holder::value).toList()).orElseGet(List::of);
    }

}