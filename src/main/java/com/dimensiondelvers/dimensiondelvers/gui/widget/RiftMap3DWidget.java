package com.dimensiondelvers.dimensiondelvers.gui.widget;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.riftmap.RiftMap;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class RiftMap3DWidget extends AbstractWidget {
    public RiftMap3DWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Component.literal("RiftMap"));
        RiftMap.setMapSize(x, y, width, height);
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int i, int i1, float v) {
        guiGraphics.renderOutline(this.getX()-1, this.getY(), this.getWidth()+1, this.getHeight(), 0xFFFFFFFF);
        RiftMap.renderMap();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0) {
            RiftMap.camPitch += (float)dragY;
            RiftMap.camPitch = Math.clamp(RiftMap.camPitch, -90, 90);
            RiftMap.camYaw += (float)dragX;
        } else if (button == 1) {
            float yawRad = (float) Math.toRadians(RiftMap.camYaw);

            RiftMap.camPos.z += (float) (-dragY * Math.cos(yawRad) - dragX * Math.sin(yawRad))/20;
            RiftMap.camPos.x += (float) (-dragY * Math.sin(yawRad) + dragX * Math.cos(yawRad))/20;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return true;
    }

    @Override
    public boolean mouseScrolled(double a, double b, double c , double d) {
        RiftMap.distance -= (float) d;
        RiftMap.distance = Math.clamp(RiftMap.distance, 1, 20);

        return super.mouseScrolled(a, b, c, d);
    }
}
