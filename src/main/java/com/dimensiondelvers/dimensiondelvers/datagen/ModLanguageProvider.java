package com.dimensiondelvers.dimensiondelvers.datagen;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import com.dimensiondelvers.dimensiondelvers.init.ModBlocks;
import com.dimensiondelvers.dimensiondelvers.init.ModItems;
import com.dimensiondelvers.dimensiondelvers.init.ModUpgrades;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

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
        addBlock(ModBlocks.RIFT_CHEST, "Rift Chest");

        // Adds an item translation.
        addItem(ModItems.EXAMPLE_ITEM, "Example Item");
        addItem(ModItems.RUNEGEM, "Runegem");

        ModBlocks.BLOCK_FAMILY_HELPERS.forEach(helper -> {
            addBlock(helper.getBlock(), getTranslationString(helper.getBlock().get()));
            helper.getVariants().forEach((variant, block) -> addBlock(block, getTranslationString(block.get())));
        });


        // Adds a generic translation
        add("itemGroup.dimensiondelvers", "Dimension Delvers");

//        add(ModAbilities.BOOST_ABILITY.get().GetTranslationString(), "Boost Ability");
//        add(ModAbilities.SUMMON_ARROW_ABILITY.get().GetTranslationString(), "Arrow Ability");
//        add(ModAbilities.HEAL_ABILITY.get().GetTranslationString(), "Heal Ability");
//        add(ModAbilities.ARMOR_STAND_ABILITY.get().GetTranslationString(), "Armorstand Ability");
//        add(ModAbilities.BE_PRETTY.get().GetTranslationString(), "Pretty Ability");
//        add(ModAbilities.BE_SMOL.get().GetTranslationString(), "Smol Ability");


//        add(ModUpgrades.UNLOCK_BOOST.get().getTranslationString(), "Unlock Boost");
//        add("tooltip." + ModUpgrades.UNLOCK_BOOST.get().getTranslationString(), "Press I to boost!");
//        add(ModUpgrades.UNLOCK_ARROW.get().getTranslationString(), "Unlock Arrow");
//        add("tooltip." + ModUpgrades.UNLOCK_ARROW.get().getTranslationString(), "Press O to shoot!");
//        add(ModUpgrades.UNLOCK_HEAL.get().getTranslationString(), "Unlock Heal");
//        add("tooltip." + ModUpgrades.UNLOCK_HEAL.get().getTranslationString(), "Press U to heal!");
//        add(ModUpgrades.UNLOCK_ARMOR_STAND.get().getTranslationString(), "Unlock Armorstand");
//        add("tooltip." + ModUpgrades.UNLOCK_ARMOR_STAND.get().getTranslationString(), "Press Y to toggle Armorstand!");
//        add(ModUpgrades.UNLOCK_PRETTY.get().getTranslationString(), "Unlock Beauty");
//        add("tooltip." + ModUpgrades.UNLOCK_PRETTY.get().getTranslationString(), "Press H to be temporarily pretty (Check F5)");

//        add(ModUpgrades.UPGRADE_BOOST.get().getTranslationString(), "Boost Faster");
//        add("tooltip." + ModUpgrades.UPGRADE_BOOST.get().getTranslationString(), "Makes your boost cooldown faster");

//        add(ModUpgrades.UNLOCK_SMOL.get().getTranslationString(), "Unlock Smol");
//        add("tooltip." + ModUpgrades.UNLOCK_SMOL.get().getTranslationString(), "Press L to shrink!");

        add("ability.dimensiondelvers.cannot_unlock", "You must unlock the following to get this boost: ");
        add("container.dimensiondelvers.rune_anvil", "Rune Anvil");
        add("container.dimensiondelvers.rift_chest", "Rift Chest");

        add("accessibility.dimensiondelvers.screen.title", "Dimension Delvers: Accessibility Settings");
        add("accessibility.dimensiondelvers.menubutton", "DimDelvers Accessibility (tmp)");
        add("accessibility.dimensiondelvers.screen.trypophobia", "Trypophobia");
        add("accessibility.dimensiondelvers.screen.arachnophobia", "Arachnophobia");
        add("accessibility.dimensiondelvers.screen.flashing_lights", "Flashing Lights");
        add("accessibility.dimensiondelvers.screen.misophonia", "Misophonia");
        add("accessibility.dimensiondelvers.screen.high_contrast", "High Contrast");
        add("accessibility.dimensiondelvers.screen.hard_of_hearing", "Hard of Hearing");
        add("accessibility.dimensiondelvers.screen.reduced_motion", "Reduced Motion");
        add("accessibility.dimensiondelvers.screen.tooltip.trypophobia", "Removes any trypophobia-triggering aspects");
        add("accessibility.dimensiondelvers.screen.tooltip.arachnophobia", "Replaces all the spiders with cute cats!");
        add("accessibility.dimensiondelvers.screen.tooltip.flashing_lights", "Reduces flashing-light effects");
        add("accessibility.dimensiondelvers.screen.tooltip.misophonia", "Replaces certain sounds that are potentially triggering with different ones (?)");
        add("accessibility.dimensiondelvers.screen.tooltip.high_contrast", "Enhances UI and HUD elements with higher contrast for better visibility");
        add("accessibility.dimensiondelvers.screen.tooltip.hard_of_hearing", "Enhances audio cues for better accessibility");
        add("accessibility.dimensiondelvers.screen.tooltip.reduced_motion", "Disables or slows down UI animations, camera shake, or screen effects");
    }

    private static @NotNull String getTranslationString(Block block) {
        String idString = BuiltInRegistries.BLOCK.getKey(block).getPath();
        StringBuilder sb = new StringBuilder();
        for (String word : idString.toLowerCase(Locale.ROOT).split("_")) {
            sb.append(word.substring(0, 1).toUpperCase(Locale.ROOT));
            sb.append(word.substring(1));
            sb.append(" ");
        }
        return sb.toString().trim();
    }
}
