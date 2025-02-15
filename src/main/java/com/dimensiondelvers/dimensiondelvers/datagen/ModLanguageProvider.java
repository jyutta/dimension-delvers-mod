package com.dimensiondelvers.dimensiondelvers.datagen;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.init.ModBlocks;
import com.dimensiondelvers.dimensiondelvers.init.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jetbrains.annotations.NotNull;

/* Handles Data Generation for I18n of the locale 'en_us' of the DimensionDelvers mod */
public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output) {
        super(output, DimensionDelvers.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        // Helpers are available for various common object types. Every helper has two variants: an add() variant
        // for the object itself, and an addTypeHere() variant that accepts a supplier for the object.
        // The different names for the supplier variants are required due to generic type erasure.
        // All following examples assume the existence of the values as suppliers of the needed type.
        // See https://docs.neoforged.net/docs/1.21.1/resources/client/i18n/ for translation of other types.

        // Adds a block translation.
        addBlock(ModBlocks.DEV_BLOCK, "Dev Block");
        addBlock(ModBlocks.RUNE_ANVIL_BLOCK, "Rune Anvil");

        // Adds an item translation.
        addItem(ModItems.EXAMPLE_ITEM, "Example Item");
        addItem(ModItems.RUNEGEM, "Runegem");

        ModBlocks.BLOCK_FAMILY_HELPERS.forEach(helper -> {
            addBlock(helper.getBlock(), getTranslationString(helper.getBlock().get()));
            helper.getVariants().forEach((variant, block) -> addBlock(block, getTranslationString(block.get())));
        });


        // Adds a generic translation
        add("itemGroup.dimensiondelvers", "Dimension Delvers");

        add("container.dimensiondelvers.rune_anvil", "Rune Anvil");
    }

    private static @NotNull String getTranslationString(Block block) {
        String idString = BuiltInRegistries.BLOCK.getKey(block).getPath();
        StringBuilder sb = new StringBuilder();
        for (String word : idString.toLowerCase().split("_")) {
            sb.append(word.substring(0, 1).toUpperCase());
            sb.append(word.substring(1));
            sb.append(" ");
        }
        return sb.toString().trim();
    }
}
