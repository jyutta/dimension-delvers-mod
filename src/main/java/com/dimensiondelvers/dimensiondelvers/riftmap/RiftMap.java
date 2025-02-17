package com.dimensiondelvers.dimensiondelvers.riftmap;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.awt.*;
import java.util.ArrayList;

@EventBusSubscriber(modid = "dimensiondelvers", value = Dist.CLIENT)
public class RiftMap {
    public static ArrayList<MapCell> cells = new ArrayList<>();
    public static int x, y, z;

    private static VirtualCamera camera = new VirtualCamera(70.0f, 16f/9f, 0.1f, 1000.0f);

    public static VirtualCamera getCamera() {
        return camera;
    }

    public static void addCell(MapCell cell) {
        cells.add(cell);
    }

    public static void removeCell(MapCell cell) {
        cells.remove(cell);
    }
    public static double i;

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void renderMap(RenderGuiEvent.Post event) {
        // bunch of rendersystem stuff, don't touch, it's radioactive and extremely volatile
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();

        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR); // prep the buffer

        RenderSystem.setShader(CoreShaders.POSITION_COLOR);

        Cube cube1 = new Cube(0.0f, 0.0f, 0.0f, 1.0f);
        Cube cube2 = new Cube(2.0f, 0.0f, 0.0f, 1.0f);
        Cube cube3 = new Cube(-2.0f, -2.0f, 2.0f, 1.0f);
        cube1.render(buffer, camera);
        cube2.render(buffer, camera);
        cube3.render(buffer, camera);

        // sets both position and rotation
        camera.orbitAroundOrigin(Minecraft.getInstance().player.getViewXRot(1.0f), -Minecraft.getInstance().player.getViewYRot(1.0f), 10.0f);

        MeshData bufferData = buffer.build();
        if (bufferData != null) {
            BufferUploader.drawWithShader(bufferData);
        }
        RenderSystem.disableBlend();
    }
}
