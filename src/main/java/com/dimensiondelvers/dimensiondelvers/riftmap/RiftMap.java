package com.dimensiondelvers.dimensiondelvers.riftmap;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.GameRenderer;
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

    private static VirtualCamera camera = new VirtualCamera(70.0f, 16f/9f, 100.0f, 1000.0f);

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
        /*if (camera == null) {
            camera = new Camera((float) Math.toRadians(60.0), 1.0f, 0.01f, 100.0f);
            camera.setPosition(0, 0, 0); // Example position
            camera.setRotation(new Quaternionf().rotateY((float) Math.toRadians(i%90))); // Example rotation
        }
        //camera.setRotation(new Quaternionf().rotateX((float) Math.toRadians((i++/4)%90))); // Example rotation

        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            camera.setRotationFromYawPitch(-player.getYRot(), -player.getXRot());
        }

        for (MapCell cell : cells) {
            // Render cell
        }*/
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.scale(2f, 2f, 2f);
        //poseStack.rotateAround(new Quaternionf().rotateX((float) Math.toRadians(30)), 50, 50, 50);// rotation kinda top down
        poseStack.rotateAround(new Quaternionf().rotateY((float) Math.PI/2), 50, 50, 50);// rotation kinda to the left

        //poseStack.mulPose(new Quaternionf().rotateX((float) Math.toRadians(10)));
        PoseStack.Pose pose = poseStack.last();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();

        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Cube cube1 = new Cube(-1.0f, 0.0f, 0.0f, 1.0f);
        cube1.render(buffer, camera);
        camera.setPosition(0, (float) i, 0);
        i += 0.005;
        if (i > 5) {
            i = 0;
        }

        /*buffer.addVertex(pose, 1.0f, 1.0f, 0f).setColor(1f, 1f, 1f, 1f);
        buffer.addVertex(pose, 1.0f, 5.0f, 0f).setColor(1f, 1f, 1f, 1f);
        buffer.addVertex(pose, 1.0f, 5.0f, 0f).setColor(1f, 1f, 1f, 1f);
        buffer.addVertex(pose, 5.0f, 5.0f, 0f).setColor(1f, 1f, 1f, 1f);
        buffer.addVertex(5.0f, 5.0f, 0f).setColor(1f, 1f, 1f, 1f);
        buffer.addVertex(5.0f, 1.0f, 0f).setColor(1f, 1f, 1f, 1f);
        buffer.addVertex(5.0f, 1.0f, 0f).setColor(1f, 1f, 1f, 1f);
        buffer.addVertex(1.0f, 1.0f, 0f).setColor(1f, 1f, 1f, 1f);

        //addCube(buffer, pose, 50f, 50f, 50f, 5f, Color.RED);
        addCube(buffer, pose, 56f, 50f, 56f, 5f, Color.GREEN);

        renderWireframeCube(poseStack, buffer, camera, 50f, 50f, 50f, 5f, new Quaternionf().rotateY((float) Math.toRadians(0)));
        renderWireframeCube(poseStack, buffer, camera, 50f, 50f, 60f, 5f, new Quaternionf().rotateY((float) Math.toRadians(0)));
        renderWireframeCube(poseStack, buffer, camera, 0f, 0f, 0f, 5f, new Quaternionf().rotateY((float) Math.toRadians(0)));*/



        MeshData bufferData = buffer.build();
        if (bufferData != null) {
            BufferUploader.drawWithShader(bufferData);
        }
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
    public static void renderWireframeCube(PoseStack poseStack, BufferBuilder buffer, VirtualCamera camera, float x, float y, float z, float m, Quaternionf rotation) {
        poseStack.pushPose(); // Save state

        // Apply transformations
        poseStack.translate(x, y, z);  // Move to position
        poseStack.mulPose(rotation);   // Apply rotation
        poseStack.scale(m, m, m);      // Scale to match size

        // Get transformation matrix
        Matrix4f modelMatrix = poseStack.last().pose();

        // Get view and projection matrices from the camera
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();

        // Combine model, view, and projection matrices
        Matrix4f mvpMatrix = new Matrix4f(projectionMatrix).mul(viewMatrix).mul(modelMatrix);


        //Matrix4f projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(45.0), 16.0f / 9.0f, 0.1f, 100.0f);

        //Matrix4f mvpMatrix = new Matrix4f(projectionMatrix).mul(matrix);

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
            addLine(buffer, mvpMatrix,
                    vertices[edge[0]][0], vertices[edge[0]][1], vertices[edge[0]][2],
                    vertices[edge[1]][0], vertices[edge[1]][1], vertices[edge[1]][2]);
        }

        poseStack.popPose(); // Restore state
    }

    private static void addLine(BufferBuilder vertexBuilder, Matrix4f matrix, float x1, float y1, float z1, float x2, float y2, float z2) {
        vertexBuilder.addVertex(matrix, x1, y1, z1).setColor(10, 255, 255, 255);
        vertexBuilder.addVertex(matrix, x2, y2, z2).setColor(255, 255, 255, 255);
    }
}
