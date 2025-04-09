package com.wanderersoftherift.wotr.gui.menu;

import com.wanderersoftherift.wotr.abilities.AbilitySlots;
import com.wanderersoftherift.wotr.abilities.AbstractAbility;
import com.wanderersoftherift.wotr.abilities.upgrade.Upgrade;
import com.wanderersoftherift.wotr.abilities.upgrade.UpgradePool;
import com.wanderersoftherift.wotr.gui.menu.slot.AbilitySlot;
import com.wanderersoftherift.wotr.gui.menu.slot.SkillThreadSlot;
import com.wanderersoftherift.wotr.init.ModBlocks;
import com.wanderersoftherift.wotr.init.ModDataComponentType;
import com.wanderersoftherift.wotr.init.ModMenuTypes;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The skill bench allows viewing and managing a skill and its upgrades
 */
public class SkillBenchMenu extends AbstractContainerMenu {
    private static final int INPUT_SLOTS = 2;
    private final static int PLAYER_INVENTORY_SLOTS = 3 * 9;
    private final static int PLAYER_SLOTS = PLAYER_INVENTORY_SLOTS + 9;

    private final ContainerLevelAccess access;
    private final SimpleContainer inputContainer;

    // TODO: Config
    // TODO: Better home
    public static final IntList COST_PER_LEVEL = new IntArrayList(new int[]{0, 1, 1, 1, 2, 2, 3, 4, 5, 7, 9, 12, 16, 21, 28});

    public SkillBenchMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL, new ItemStackHandler(AbilitySlots.ABILITY_BAR_SIZE));
    }

    public SkillBenchMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access, IItemHandler abilities) {
        super(ModMenuTypes.SKILL_BENCH_MENU.get(), containerId);
        this.access = access;
        this.inputContainer = new SimpleContainer(INPUT_SLOTS);
        inputContainer.addListener(this::onAbilitySlotChanged);
        addSlot(new AbilitySlot(inputContainer, 0, 32, 17));
        addSlot(new SkillThreadSlot(inputContainer, 1, 297, 7));

        addStandardInventorySlots(playerInventory, 32, 154);
        addPlayerSkillSlots(abilities, 4, 46);
    }

    private void onAbilitySlotChanged(Container container) {
        access.execute((level, blockPos) -> {
            ItemStack item = container.getItem(0);

            if (!item.isEmpty() && !item.has(ModDataComponentType.UPGRADE_POOL)) {
                Holder<AbstractAbility> ability = item.get(ModDataComponentType.ABILITY);
                RegistryAccess registryAccess = level.registryAccess();

                UpgradePool.Mutable upgradePool = new UpgradePool.Mutable();
                upgradePool.generateChoices(registryAccess, ability.value(), 3, level.random, UpgradePool.SELECTION_PER_LEVEL);
                DataComponentPatch patch = DataComponentPatch.builder().set(ModDataComponentType.UPGRADE_POOL.get(), upgradePool.toImmutable()).build();
                item.applyComponents(patch);
            }
        });
    }

    protected void addPlayerSkillSlots(IItemHandler abilitySlots, int x, int y) {
        for (int i = 0; i < abilitySlots.getSlots(); i++) {
            addSlot(new SlotItemHandler(abilitySlots, i, x, y + i * 18));
        }
    }

    public boolean isAbilityItemPresent() {
        ItemStack item = inputContainer.getItem(0);
        return !item.isEmpty() && item.has(ModDataComponentType.ABILITY);
    }

    public ItemStack getAbilityItem() {
        return inputContainer.getItem(0);
    }

    public @Nullable Holder<AbstractAbility> getAbility() {
        ItemStack item = getAbilityItem();
        return item.get(ModDataComponentType.ABILITY);
    }

    public @Nullable UpgradePool getUpgradePool() {
        ItemStack item = getAbilityItem();
        if (!item.isEmpty() && item.has(ModDataComponentType.UPGRADE_POOL)) {
            return item.get(ModDataComponentType.UPGRADE_POOL);
        }
        return null;
    }

    public int availableThread() {
        return inputContainer.getItem(1).getCount();
    }

    public boolean canLevelUp() {
        UpgradePool pool = getUpgradePool();
        if (pool != null) {
            return pool.getChoiceCount() < COST_PER_LEVEL.size();
        }
        return false;
    }

    public int costForNextLevel() {
        UpgradePool pool = getUpgradePool();
        if (pool != null) {
            return COST_PER_LEVEL.getInt(pool.getChoiceCount());
        }
        return 65;
    }

    public void levelUp(int level) {
        UpgradePool pool = getUpgradePool();
        if (pool == null || level != pool.getChoiceCount() + 1 || !canLevelUp()) {
            return;
        }
        int available = availableThread();
        int cost = costForNextLevel();
        if (available < cost) {
            return;
        }
        access.execute((serverLevel, pos) -> {
            inputContainer.getItem(1).shrink(cost);
            getAbilityItem().set(ModDataComponentType.UPGRADE_POOL, pool.getMutable().generateChoice(serverLevel.registryAccess(), getAbility().value(), serverLevel.getRandom(), UpgradePool.SELECTION_PER_LEVEL).toImmutable());
        });
    }

    public int getUnlockLevel() {
        UpgradePool upgradePool = getUpgradePool();
        if (upgradePool != null) {
            return upgradePool.getChoiceCount();
        }
        return 0;
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.access.execute((world, pos) -> this.clearContainer(player, inputContainer));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            ItemStack originalStack = slotStack.copy();
            if (slot instanceof AbilitySlot) {
                if (!this.moveItemStackTo(slotStack, INPUT_SLOTS, INPUT_SLOTS + PLAYER_SLOTS, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, originalStack);
            } else if (slot instanceof SkillThreadSlot) {
                if (!this.moveItemStackTo(slotStack, INPUT_SLOTS, INPUT_SLOTS + PLAYER_SLOTS, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, originalStack);
            } else if (index < INPUT_SLOTS + PLAYER_SLOTS) {
                if (!this.moveItemStackTo(slotStack, 0, INPUT_SLOTS, false)) {
                    // Move from player inventory to hotbar
                    if (index < INPUT_SLOTS + PLAYER_INVENTORY_SLOTS) {
                        if (!this.moveItemStackTo(slotStack, INPUT_SLOTS + PLAYER_INVENTORY_SLOTS, INPUT_SLOTS + PLAYER_SLOTS, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                    // Move from hotbar to player inventory
                    else if (!this.moveItemStackTo(slotStack, INPUT_SLOTS, INPUT_SLOTS + PLAYER_INVENTORY_SLOTS, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                if (!this.moveItemStackTo(slotStack, INPUT_SLOTS, INPUT_SLOTS + PLAYER_SLOTS, true)) {
                    return ItemStack.EMPTY;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.access, player, ModBlocks.SKILL_BENCH.get());
    }

    public void selectSkill(int choice, int selection) {
        if (isAbilityItemPresent()) {
            UpgradePool pool = getUpgradePool();
            if (choice < 0 || choice >= pool.getChoiceCount()) {
                return;
            }
            List<Holder<Upgrade>> options = pool.getChoice(choice);
            if (selection < 0 || selection >= options.size()) {
                return;
            }

            UpgradePool.Mutable mutable = pool.getMutable();
            mutable.selectChoice(choice, selection);
            DataComponentPatch patch = DataComponentPatch.builder().set(ModDataComponentType.UPGRADE_POOL.get(), mutable.toImmutable()).build();
            getAbilityItem().applyComponents(patch);
        }
    }
}
