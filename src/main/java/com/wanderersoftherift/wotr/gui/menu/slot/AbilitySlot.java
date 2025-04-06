package com.wanderersoftherift.wotr.gui.menu.slot;

import com.wanderersoftherift.wotr.init.ModDataComponentType;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class AbilitySlot extends Slot {

    public AbilitySlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.has(ModDataComponentType.ABILITY);
    }

}
