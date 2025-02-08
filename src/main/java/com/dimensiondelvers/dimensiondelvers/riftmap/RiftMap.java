package com.dimensiondelvers.dimensiondelvers.riftmap;

import com.dimensiondelvers.dimensiondelvers.events.RenderEvents;
import com.google.common.eventbus.Subscribe;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

@EventBusSubscriber(modid = "dimensiondelvers", value = Dist.CLIENT)
public class RiftMap {
    public static ArrayList<MapCell> cells = new ArrayList<>();
    public static int x, y, z;

    public static void addCell(MapCell cell) {
        cells.add(cell);
    }

    public static void removeCell(MapCell cell) {
        cells.remove(cell);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void renderMap(RenderGuiEvent.Post event) {
        for (MapCell cell : cells) {
            // Render cell
        }
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.scale(2f, 2f, 2f);
        //poseStack.rotateAround(new Quaternionf().rotateX((float) Math.toRadians(30)), 50, 50, 50);// rotation kinda top down
        poseStack.rotateAround(new Quaternionf().rotateY((float) Math.PI/2), 50, 50, 50);// rotation kinda to the left

        //poseStack.mulPose(new Quaternionf().rotateX((float) Math.toRadians(10)));
        PoseStack.Pose pose = poseStack.last();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        buffer.addVertex(pose, 1.0f, 1.0f, 0f).setColor(1f, 1f, 1f, 1f);
        buffer.addVertex(pose, 1.0f, 5.0f, 0f).setColor(1f, 1f, 1f, 1f);
        buffer.addVertex(pose, 1.0f, 5.0f, 0f).setColor(1f, 1f, 1f, 1f);
        buffer.addVertex(pose, 5.0f, 5.0f, 0f).setColor(1f, 1f, 1f, 1f);
        buffer.addVertex(5.0f, 5.0f, 0f).setColor(1f, 1f, 1f, 1f);
        buffer.addVertex(5.0f, 1.0f, 0f).setColor(1f, 1f, 1f, 1f);
        buffer.addVertex(5.0f, 1.0f, 0f).setColor(1f, 1f, 1f, 1f);
        buffer.addVertex(1.0f, 1.0f, 0f).setColor(1f, 1f, 1f, 1f);

        //addCube(buffer, pose, 50f, 50f, 50f, 5f, Color.RED);
        addCube(buffer, pose, 56f, 50f, 56f, 5f, Color.GREEN);

        renderWireframeCube(poseStack, buffer, 50f, 50f, 50f, 5f, new Quaternionf().rotateY((float) Math.toRadians(30)));


        BufferUploader.drawWithShader(buffer.buildOrThrow());
        RenderSystem.disableBlend();
    }

    private static void addCube(BufferBuilder buffer, PoseStack.Pose pose, float x, float y, float z, float size, Color color) {
        buffer.addVertex(pose, x, y, z).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.addVertex(pose, x + size, y, z).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        buffer.addVertex(pose, x + size, y, z).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.addVertex(pose, x + size, y + size, z).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        buffer.addVertex(pose, x + size, y + size, z).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.addVertex(pose, x, y + size, z).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        buffer.addVertex(pose, x, y + size, z).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.addVertex(pose, x, y, z).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        buffer.addVertex(pose, x, y, z).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.addVertex(pose, x, y, z + size).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        buffer.addVertex(pose, x + size, y, z).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.addVertex(pose, x + size, y, z + size).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        buffer.addVertex(pose, x + size, y + size, z).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.addVertex(pose, x + size, y + size, z + size).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        buffer.addVertex(pose, x, y + size, z).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.addVertex(pose, x, y + size, z + size).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        buffer.addVertex(pose, x, y, z + size).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.addVertex(pose, x + size, y, z + size).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        buffer.addVertex(pose, x + size, y, z + size).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.addVertex(pose, x + size, y + size, z + size).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        buffer.addVertex(pose, x + size, y + size, z + size).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.addVertex(pose, x, y + size, z + size).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        buffer.addVertex(pose, x, y + size, z + size).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.addVertex(pose, x, y, z + size).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    public static void renderWireframeCube(PoseStack poseStack, BufferBuilder buffer, float x, float y, float z, float m, Quaternionf rotation) {
        poseStack.pushPose(); // Save state

        // Apply transformations
        poseStack.translate(x, y, z);  // Move to position
        poseStack.mulPose(rotation);   // Apply rotation
        poseStack.scale(m, m, m);      // Scale to match size

        // Get transformation matrix
        Matrix4f matrix = poseStack.last().pose();

        // Get buffer for wireframe (DEBUG_LINES)


        // Define cube vertices
        float[][] vertices = {
                { -0.5f, -0.5f, -0.5f }, { 0.5f, -0.5f, -0.5f },
                { 0.5f, -0.5f,  0.5f }, { -0.5f, -0.5f,  0.5f },
                { -0.5f,  0.5f, -0.5f }, { 0.5f,  0.5f, -0.5f },
                { 0.5f,  0.5f,  0.5f }, { -0.5f,  0.5f,  0.5f }
        };

        // Define cube edges (each pair is a line)
        int[][] edges = {
                { 0, 1 }, { 1, 2 }, { 2, 3 }, { 3, 0 }, // Bottom square
                { 4, 5 }, { 5, 6 }, { 6, 7 }, { 7, 4 }, // Top square
                { 0, 4 }, { 1, 5 }, { 2, 6 }, { 3, 7 }  // Vertical edges
        };

        // Draw each line
        for (int[] edge : edges) {
            addLine(buffer, matrix,
                    vertices[edge[0]][0], vertices[edge[0]][1], vertices[edge[0]][2],
                    vertices[edge[1]][0], vertices[edge[1]][1], vertices[edge[1]][2]);
        }

        poseStack.popPose(); // Restore state
    }

    private static void addLine(BufferBuilder vertexBuilder, Matrix4f matrix, float x1, float y1, float z1, float x2, float y2, float z2) {
        vertexBuilder.addVertex(matrix, x1, y1, z1).setColor(255, 255, 255, 255);
        vertexBuilder.addVertex(matrix, x2, y2, z2).setColor(255, 255, 255, 255);
    }
}
