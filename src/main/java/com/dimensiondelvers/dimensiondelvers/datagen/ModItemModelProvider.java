package com.dimensiondelvers.dimensiondelvers.datagen;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.init.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/* Handles Data Generation for Item models of the DimensionDelvers mod */
public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DimensionDelvers.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.EXAMPLE_ITEM.get());
    }
}
