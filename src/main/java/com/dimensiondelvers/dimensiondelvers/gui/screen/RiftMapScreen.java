package com.dimensiondelvers.dimensiondelvers.gui.screen;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.mojang.blaze3d.platform.InputConstants;
import cpw.mods.util.Lazy;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public class RiftMapScreen extends Screen {
    public RiftMapScreen(Component title) {
        super(title);
    }


    @Override
    protected void init() {
        super.init();

        //this.addRenderableWidget();
    }

    // In some Screen subclass
    @Override
    public boolean mouseClicked(double x, double y, int button) {
        DimensionDelvers.LOGGER.info("clicked da mause at "+ x+ y+ button);
        return super.mouseClicked(x, y, button);
    }

    @Override
    public boolean mouseReleased(double x, double y, int button) {
        DimensionDelvers.LOGGER.info("unclicked da mause at "+ x+ y+ button);
        return super.mouseReleased(x, y, button);
    }
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        DimensionDelvers.LOGGER.info("dragged da mause at "+ mouseX+ mouseY+ button + dragX+dragY);
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);

        super.render(graphics, mouseX, mouseY, partialTick);
    }
}
