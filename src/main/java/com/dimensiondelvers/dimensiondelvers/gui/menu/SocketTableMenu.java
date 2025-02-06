package com.dimensiondelvers.dimensiondelvers.gui.menu;

import com.dimensiondelvers.dimensiondelvers.init.ModBlocks;
import com.dimensiondelvers.dimensiondelvers.init.ModDataComponentType;
import com.dimensiondelvers.dimensiondelvers.init.ModItems;
import com.dimensiondelvers.dimensiondelvers.init.ModMenuTypes;
import com.dimensiondelvers.dimensiondelvers.item.socket.GearSocket;
import com.dimensiondelvers.dimensiondelvers.item.socket.GearSockets;
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
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SocketTableMenu extends AbstractContainerMenu {
    private static final int GEAR_SLOT = 36;
    private static final Vector2i GEAR_SLOT_POSITION = new Vector2i(80, 76);
    private static final List<Integer> RUNE_SLOTS = List.of(37, 38, 39, 40, 41, 42, 43, 44);
    public static final List<Vector2i> RUNE_SLOT_POSITIONS = List.of( // CLOCKWISE FROM TOP CENTER
            new Vector2i(80, 22),
            new Vector2i(116, 40),
            new Vector2i(134, 76),
            new Vector2i(116, 112),
            new Vector2i(80, 130),
            new Vector2i(44, 112),
            new Vector2i(26, 76),
            new Vector2i(44, 40)
    );

    private Inventory playerInventory;
    private ContainerLevelAccess access;
    private Container gearSlotContainer;
    private Container socketSlotsContainer;

    // Client
    public SocketTableMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    // Server
    public SocketTableMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(ModMenuTypes.SOCKET_TABLE_MENU.get(), containerId);
        this.playerInventory = playerInventory;
        this.access = access;
        this.createInventorySlots(playerInventory);
        this.createGearSlot();
        this.createSocketSlots();
        for (int i = 0; i < 8; i++) {
            this.socketSlotsContainer.setItem(i, Items.BARRIER.getDefaultInstance());
        }
    }

    private void createInventorySlots(Inventory inventory) {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 166 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 224));
        }
    }

    private void createGearSlot() {
        this.gearSlotContainer = createContainer(1);
        this.addSlot(new Slot(this.gearSlotContainer, 0, GEAR_SLOT_POSITION.x, GEAR_SLOT_POSITION.y) {
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getCount() == 1;
            }
        });
    }

    private void createSocketSlots() {
        this.socketSlotsContainer = createContainer(8);
        for (int i = 0; i < 8; i++) {
            Vector2i position = RUNE_SLOT_POSITIONS.get(i);
            this.addSlot(new Slot(this.socketSlotsContainer, i, position.x, position.y) {
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.is(ModItems.RUNEGEM) && stack.getCount() == 1;
                }

                public boolean mayPickup(@NotNull Player player) {
                    return !getItem().is(Items.BARRIER);
                }
            });
        }
    }

    public void slotsChanged(Container inventory) {
        super.slotsChanged(inventory);
        if (inventory == this.gearSlotContainer) {
            ItemStack gear = this.gearSlotContainer.getItem(0);
            if (gear.isEmpty()) {
                for (int i = 0; i < 8; i++) {
                    this.socketSlotsContainer.setItem(i, Items.BARRIER.getDefaultInstance());
                }
            } else {
                Random random = new Random();
                int amount = random.nextInt(8) + 1;
                for (int i = 0; i < amount; i++) {
                    this.socketSlotsContainer.setItem(i, ItemStack.EMPTY);
                }
                for (int i = amount; i < 8; i++) {
                    this.socketSlotsContainer.setItem(i, Items.BARRIER.getDefaultInstance());
                }
            }
        }
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
        return access.evaluate((world, pos) -> !isValidBlock(world.getBlockState(pos)) ? false : player.canInteractWithBlock(pos, 4.0F), true);
    }

    protected boolean isValidBlock(BlockState state) {
        return state.is(ModBlocks.SOCKET_TABLE_BLOCK);
    }
}
