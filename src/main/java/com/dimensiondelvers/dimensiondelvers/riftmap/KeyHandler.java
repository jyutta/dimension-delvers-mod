package com.dimensiondelvers.dimensiondelvers.riftmap;

import com.llamalad7.mixinextras.lib.apache.commons.ArrayUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = "dimensiondelvers")
public class KeyHandler {
    private static final KeyMapping moveForward = new KeyMapping("key.dimensiondelvers.move_forward", GLFW.GLFW_KEY_W, "key.categories.dimensiondelvers");
    private static final KeyMapping moveBackward = new KeyMapping("key.dimensiondelvers.move_backward", GLFW.GLFW_KEY_S, "key.categories.dimensiondelvers");
    private static final KeyMapping moveLeft = new KeyMapping("key.dimensiondelvers.move_left", GLFW.GLFW_KEY_A, "key.categories.dimensiondelvers");
    private static final KeyMapping moveRight = new KeyMapping("key.dimensiondelvers.move_right", GLFW.GLFW_KEY_D, "key.categories.dimensiondelvers");
    private static final KeyMapping moveUp = new KeyMapping("key.dimensiondelvers.move_up", GLFW.GLFW_KEY_SPACE, "key.categories.dimensiondelvers");
    private static final KeyMapping moveDown = new KeyMapping("key.dimensiondelvers.move_down", GLFW.GLFW_KEY_LEFT_SHIFT, "key.categories.dimensiondelvers");

    public static void register() {
        Minecraft.getInstance().options.keyMappings = ArrayUtils.addAll(Minecraft.getInstance().options.keyMappings, moveForward, moveBackward, moveLeft, moveRight, moveUp, moveDown);
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Camera camera = RiftMap.getCamera();
        if (moveForward.isDown()) camera.move(0, 0, -1f);
        if (moveBackward.isDown()) camera.move(0, 0, 1f);
        if (moveLeft.isDown()) camera.move(1f, 0, 0);
        if (moveRight.isDown()) camera.move(-1f, 0, 0);
        if (moveUp.isDown()) camera.move(0, 1f, 0);
        if (moveDown.isDown()) camera.move(0, -1f, 0);
    }
}