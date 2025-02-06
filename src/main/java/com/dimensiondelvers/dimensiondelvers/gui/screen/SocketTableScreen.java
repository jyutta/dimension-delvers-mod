package com.dimensiondelvers.dimensiondelvers.gui.screen;

import com.dimensiondelvers.dimensiondelvers.gui.menu.SocketTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public class SocketTableScreen extends AbstractContainerScreen<SocketTableMenu> implements ContainerListener {
    private static final ResourceLocation BACKGROUND = ResourceLocation.withDefaultNamespace("textures/gui/container/socket_table.png");
    private static final ResourceLocation SLOT = ResourceLocation.withDefaultNamespace("textures/gui/container/slot.png");

    public SocketTableScreen(SocketTableMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 248;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderSlot(@NotNull GuiGraphics guiGraphics, @NotNull Slot slot) {
        if (!slot.isFake()) {
            int x = slot.x - 1;
            int y = slot.y - 1;
            guiGraphics.blit(SLOT, x, y, 0, 0, 18, 18);
        }
        super.renderSlot(guiGraphics, slot);
    }

    @Override
    public void slotChanged(@NotNull AbstractContainerMenu abstractContainerMenu, int i, @NotNull ItemStack itemStack) {

    }

    @Override
    public void dataChanged(@NotNull AbstractContainerMenu abstractContainerMenu, int i, int i1) {

    }
}
