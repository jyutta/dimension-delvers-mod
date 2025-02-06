package com.dimensiondelvers.dimensiondelvers.init;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.block.SocketTableBlock;
import com.dimensiondelvers.dimensiondelvers.item.runegem.RuneGemTier;
import com.dimensiondelvers.dimensiondelvers.item.runegem.Runegem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import static com.dimensiondelvers.dimensiondelvers.DimensionDelvers.tagId;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DimensionDelvers.MODID);


    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "example_block",
            ModBlocks.EXAMPLE_BLOCK
    );

    public static final DeferredItem<BlockItem> DEV_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "dev_block",
            ModBlocks.DEV_BLOCK
    );

    public static final DeferredItem<BlockItem> SOCKET_TABLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "socket_table",
            ModBlocks.SOCKET_TABLE_BLOCK
    );

    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem(
            "example_item",
            new Item.Properties().food(new FoodProperties.Builder()
                    .alwaysEdible()
                    .nutrition(1)
                    .saturationModifier(2f)
                    .build()
            )
    );

    //Runegems
    public static final DeferredItem<Item> RAW_ATTACK_RUNE = registerRunegem("attack_rune", RuneGemTier.RAW);
    public static final DeferredItem<Item> CUT_ATTACK_RUNE = registerRunegem("attack_rune", RuneGemTier.CUT);
    public static final DeferredItem<Item> FRAMED_ATTACK_RUNE = registerRunegem("attack_rune", RuneGemTier.FRAMED);

    private static @NotNull DeferredItem<Item> registerRunegem(String name, RuneGemTier tier) {
        String fullName = tier.getName() + "_" + name;
        return ITEMS.registerItem(
                fullName,
                (properties) -> new Runegem(properties, tagId(Registries.ENCHANTMENT, fullName)));
    }

}
