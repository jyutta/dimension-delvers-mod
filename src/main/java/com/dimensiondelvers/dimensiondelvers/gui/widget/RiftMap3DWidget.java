package com.dimensiondelvers.dimensiondelvers.gui.widget;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.client.render.MapRenderer3D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.neoforged.neoforge.common.NeoForgeConfig;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.net.SocketOptions;
import java.nio.Buffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class RiftMap3DWidget extends AbstractWidget {
    private static final double MIN_SPEED = 0.5;
    private static final double MAX_SPEED = 5.0;
    private static final float MIN_DISTANCE = 1.0f;
    private static final float MAX_DISTANCE = 50.0f;

    private Vector2d targetMousePos = new Vector2d(0, 0);
    private boolean setMouse = true;
    private boolean pressingButton = false;

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
        guiGraphics.renderOutline(this.getX() - 1, this.getY(), this.getWidth() + 1, this.getHeight(), 0xFFFFFFFF);
        guiGraphics.drawString(Minecraft.getInstance().font, Minecraft.getInstance().fpsString, this.getX(), this.getY(), 0xFFFFFFFF);
        mapRenderer.renderMap();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (!setMouse) {
            int invertY = Minecraft.getInstance().options.invertYMouse().get() ? -1 : 1;
            
            if (button == 0) {
                mapRenderer.camPitch += (float) dragY * invertY;
                mapRenderer.camPitch = Math.clamp(mapRenderer.camPitch, -90, 90);
                mapRenderer.camYaw += (float) dragX;
            } else if (button == 1) {
                float yawRad = (float) Math.toRadians(mapRenderer.camYaw);
                float speed = (float) Mth.map(mapRenderer.distance, MIN_DISTANCE, MAX_DISTANCE, MIN_SPEED, MAX_SPEED);

                mapRenderer.camPos.z += (float) (-dragY * invertY * speed * Math.cos(yawRad) - dragX * speed * Math.sin(yawRad)) / 20;
                mapRenderer.camPos.x += (float) (-dragY * invertY * speed * Math.sin(yawRad) + dragX * speed * Math.cos(yawRad)) / 20;
            }
            setMouse = true;
        }
        else {
            GLFW.glfwSetCursorPos(Minecraft.getInstance().getWindow().getWindow(), targetMousePos.x, targetMousePos.y);
            setMouse = false;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!pressingButton) {
            DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

            GLFW.glfwGetCursorPos(Minecraft.getInstance().getWindow().getWindow(), x, y);

            targetMousePos = new Vector2d(x.get(), y.get());
            GLFW.glfwSetInputMode(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
        }

        pressingButton = true;

        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        pressingButton = false;

        GLFW.glfwSetInputMode(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double a, double b, double c , double d) {
        mapRenderer.distance -= (float) d;
        mapRenderer.distance = Math.clamp(mapRenderer.distance, MIN_DISTANCE, MAX_DISTANCE);

        return super.mouseScrolled(a, b, c, d);
    }
}
