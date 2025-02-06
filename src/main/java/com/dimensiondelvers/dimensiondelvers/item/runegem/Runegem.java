package com.dimensiondelvers.dimensiondelvers.item.runegem;

import com.dimensiondelvers.dimensiondelvers.init.ModDataComponentType;
import com.dimensiondelvers.dimensiondelvers.item.socket.GearSocket;
import com.dimensiondelvers.dimensiondelvers.item.socket.GearSockets;
import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class Runegem extends Item {
    public Runegem(Properties properties) {
        super(properties);
    }


    //Temporary testing code
    public boolean onStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (action != ClickAction.SECONDARY) {
            return false;
        } else {
            RunegemData runegemData = stack.get(ModDataComponentType.RUNEGEM_DATA);
            if (runegemData == null) {
                return false;
            } else {
                ItemStack targetStack = slot.getItem();
                if (stack.getCount() != 1 || targetStack.isEmpty()) {
                    return false;
                } else {
                    GearSockets gearSockets = targetStack.get(ModDataComponentType.GEAR_SOCKETS);
                    if (gearSockets == null) {
                        return false;
                    } else {
                        boolean applied = this.applyRunegemToItem(stack, targetStack, runegemData, gearSockets);
                        if (!applied) {
                            return false;
                        }
                        stack.shrink(1);
                        return true;
                    }
                }
            }
        }
    }

    private boolean applyRunegemToItem(ItemStack stack, ItemStack targetStack, RunegemData runegemData, GearSockets gearSockets) {
        List<GearSocket> sockets = gearSockets.sockets();
        int idx = 0;
        for (GearSocket socket : sockets) {
            if (!socket.canBeApplied(runegemData)) {
                idx++;
                continue;
            }
            sockets.set(idx, new GearSocket(socket.runeGemShape(), null, stack));
            targetStack.set(ModDataComponentType.GEAR_SOCKETS, new GearSockets(sockets));
            LogUtils.getLogger().info("Applied Runegem to item");
            return true;
        }
        return false;
    }

}
