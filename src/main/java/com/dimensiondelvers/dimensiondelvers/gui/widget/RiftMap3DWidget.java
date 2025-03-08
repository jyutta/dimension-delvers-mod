package com.dimensiondelvers.dimensiondelvers.gui.widget;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.client.render.MapRenderer3D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class RiftMap3DWidget extends AbstractWidget {
    private MapRenderer3D mapRenderer;
    public RiftMap3DWidget(int x, int y, int width, int height, float renderDistance) {
        super(x, y, width, height, Component.literal("mapRenderer"));
        mapRenderer = new MapRenderer3D(x, y, width, height, renderDistance);
    }

    public void resetCam() {
        mapRenderer.resetCam();
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int i, int i1, float v) {
        guiGraphics.renderOutline(this.getX()-1, this.getY(), this.getWidth()+1, this.getHeight(), 0xFFFFFFFF);
        guiGraphics.drawString(Minecraft.getInstance().font, Minecraft.getInstance().fpsString, this.getX(), this.getY(), 0xFFFFFFFF);
        mapRenderer.renderMap();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0) {
            mapRenderer.camPitch += (float)dragY;
            mapRenderer.camPitch = Math.clamp(mapRenderer.camPitch, -90, 90);
            mapRenderer.camYaw += (float)dragX;
        } else if (button == 1) {
            float yawRad = (float) Math.toRadians(mapRenderer.camYaw);

            mapRenderer.camPos.z += (float) (-dragY * Math.cos(yawRad) - dragX * Math.sin(yawRad))/20;
            mapRenderer.camPos.x += (float) (-dragY * Math.sin(yawRad) + dragX * Math.cos(yawRad))/20;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return true;
    }

    @Override
    public boolean mouseScrolled(double a, double b, double c , double d) {
        mapRenderer.distance -= (float) d;
        mapRenderer.distance = Math.clamp(mapRenderer.distance, 1, 50);

        return super.mouseScrolled(a, b, c, d);
    }
}
