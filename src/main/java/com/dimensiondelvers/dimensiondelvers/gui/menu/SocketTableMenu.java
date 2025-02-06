package com.dimensiondelvers.dimensiondelvers.gui.menu;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.init.ModBlocks;
import com.dimensiondelvers.dimensiondelvers.init.ModDataComponentType;
import com.dimensiondelvers.dimensiondelvers.init.ModItems;
import com.dimensiondelvers.dimensiondelvers.init.ModMenuTypes;
import com.dimensiondelvers.dimensiondelvers.item.socket.GearSocket;
import com.dimensiondelvers.dimensiondelvers.item.socket.GearSockets;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocketTableMenu extends AbstractContainerMenu {
//    private static final int GEAR_SLOT = 36;
    private static final Vector2i GEAR_SLOT_POSITION = new Vector2i(80, 76);
//    private static final List<Integer> RUNE_SLOTS = List.of(37, 38, 39, 40, 41, 42, 43, 44);
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
    public int activeSocketSlots = 0;

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
            int finalI = i;
            this.addSlot(new Slot(this.socketSlotsContainer, finalI, position.x, position.y) {
                private final int index = finalI;

                public boolean mayPlace(@NotNull ItemStack stack) {
                    if (this.isFake()) return false;
                    return stack.is(ModItems.RUNEGEM) && stack.getCount() == 1;
                }

                public boolean isHighlightable() {
                    return this.index < activeSocketSlots;
                }

                public boolean isFake() {
                    return this.index >= activeSocketSlots;
                }
            });
        }
    }

    public void slotsChanged(@NotNull Container inventory) {
        super.slotsChanged(inventory);
        if (inventory == this.gearSlotContainer) {
            ItemStack gear = this.gearSlotContainer.getItem(0);
            if (gear.isEmpty()) {
                this.activeSocketSlots = 0;
                for (int i = 0; i < 8; i++) {
                    this.socketSlotsContainer.setItem(i, ItemStack.EMPTY);
                }
            } else {
                this.activeSocketSlots = 3;
                ItemLore lore = gear.get(DataComponents.LORE);
                if (lore == null) {
                    return;
                }
                List<Component> loreLines = lore.lines();
                if (loreLines.isEmpty()) {
                    return;
                }
                Component firstLine = loreLines.getFirst();
                if (firstLine == null) {
                    return;
                }
                Pattern pattern = Pattern.compile("Socketed with (\\d+)/(\\d+) runes");
                Matcher matcher = pattern.matcher(firstLine.getString());
                if (matcher.find()) {
                    if (matcher.groupCount() != 2 || matcher.group(1) == null || matcher.group(2) == null) {
                        return;
                    }
                    int activeSocketSlots = Integer.parseInt(matcher.group(1));
                    int maxSocketSlots = Integer.parseInt(matcher.group(2));
                    if (activeSocketSlots > maxSocketSlots) {
                        activeSocketSlots = maxSocketSlots;
                    }
                    this.activeSocketSlots = maxSocketSlots;
                    for (int i = 0; i < activeSocketSlots; i++) {
                        this.socketSlotsContainer.setItem(i, ModItems.RUNEGEM.toStack());
                    }
                }
            }
        } else if (inventory == this.socketSlotsContainer) {
            ItemStack gear = this.gearSlotContainer.getItem(0);
            if (gear.isEmpty()) {
                return;
            }
            List<ItemStack> runes = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                ItemStack rune = this.socketSlotsContainer.getItem(i);
                if (!rune.isEmpty()) {
                    runes.add(rune);
                }
            }
            gear.set(DataComponents.LORE, new ItemLore(List.of(Component.literal("Socketed with " + runes.size() + "/3 runes"))));
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
