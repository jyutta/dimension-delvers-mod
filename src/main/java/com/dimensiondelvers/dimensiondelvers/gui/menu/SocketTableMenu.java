package com.dimensiondelvers.dimensiondelvers.gui.menu;

import com.dimensiondelvers.dimensiondelvers.init.ModBlocks;
import com.dimensiondelvers.dimensiondelvers.init.ModMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SocketTableMenu extends AbstractContainerMenu {
    private Inventory playerInventory;
    private ContainerLevelAccess access;
    private Container gearSlot;
    @Nullable
    private Container socketSlots;

    // Client
    public SocketTableMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    // Server
    public SocketTableMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(ModMenuTypes.SOCKET_TABLE_MENU.get(), containerId);
        this.playerInventory = playerInventory;
        this.access = access;
        this.gearSlot = createContainer(1);
        this.createInventorySlots(playerInventory);

    }

    private void createInventorySlots(Inventory inventory) {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 174 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 232));
        }
    }

    private void createGearSlot() {
        this.addSlot(new Slot(this.gearSlot, 0, 80, 84) {
            public boolean mayPlace(@NotNull ItemStack stack) {
                return true;
            }
        });
    }

    private Container createContainer(int size) {
        return new SimpleContainer(size) {
            public void setChanged() {
                super.setChanged();
                SocketTableMenu.this.slotsChanged(this);
            }
        };
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        return new ItemStack(Items.STICK);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return false;
    }
}
