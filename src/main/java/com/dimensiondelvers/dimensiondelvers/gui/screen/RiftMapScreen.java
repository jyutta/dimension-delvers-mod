package com.dimensiondelvers.dimensiondelvers.gui.screen;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.riftmap.RiftMap;
import com.mojang.blaze3d.platform.InputConstants;
import cpw.mods.util.Lazy;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class RiftMapScreen extends Screen {
    public RiftMapScreen(Component title) {
        super(title);

        RiftMap.camPitch = 0;
        RiftMap.camYaw = 0;

        RiftMap.camPos = new Vector3f(0.5f);
        //this.minecraft = Minecraft.getInstance();
    }


    @Override
    protected void init() {
        super.init();

        this.addRenderableWidget(new EditBox(font, 100, 10, Component.literal("TEST")));
    }

    // In some Screen subclass
    @Override
    public boolean mouseClicked(double x, double y, int button) {
        //DimensionDelvers.LOGGER.info("clicked da mause at "+ x+ y+ button);
        return super.mouseClicked(x, y, button);
    }

    @Override
    public boolean mouseReleased(double x, double y, int button) {
        //DimensionDelvers.LOGGER.info("unclicked da mause at "+ x+ y+ button);
        return super.mouseReleased(x, y, button);
    }
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        //DimensionDelvers.LOGGER.info("dragged da mause at %f %f %d %f %f".formatted(mouseX, mouseY, button , dragX,dragY));
        if (button == 0) {
            RiftMap.camPitch += (float)dragY;
            RiftMap.camYaw += (float)dragX;
        } else if (button == 1) {
            float yawRad = (float) Math.toRadians(RiftMap.camYaw);

            RiftMap.camPos.z += (float) (-dragY * Math.cos(yawRad) - dragX * Math.sin(yawRad))/20;
            RiftMap.camPos.x += (float) (-dragY * Math.sin(yawRad) + dragX * Math.cos(yawRad))/20;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double a, double b, double c, double d) {
        //DimensionDelvers.LOGGER.info("moved da wheel at %f %f %f %f".formatted(a, b, c, d));

        RiftMap.distance -= (float) d;

        return super.mouseScrolled(a, b, c, d);
    }

    @Override
    protected boolean shouldNarrateNavigation() {
        return false;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        RiftMap.renderMap();
    }

}
