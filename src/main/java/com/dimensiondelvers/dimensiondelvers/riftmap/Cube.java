package com.dimensiondelvers.dimensiondelvers.riftmap;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.Minecraft;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Cube class for RiftMap 3D renderer
 * Holds the position and size of a cube and contains the render method used to draw it
 */
public class Cube {
    public float x;
    public float y;
    public float z;
    public float size;

    public Cube(float x, float y, float z, float size) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;
    }

    private static Vector3f projectPoint(Vector3f worldPos, VirtualCamera camera) {
        int SCREEN_WIDTH = Minecraft.getInstance().getWindow().getGuiScaledWidth(); // this is the width and height of the window we want to draw
        int SCREEN_HEIGHT = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        Matrix4f mvpMatrix = new Matrix4f()
                .set(camera.getProjectionMatrix())
                .mul(camera.getViewMatrix());

        Vector4f transformed = new Vector4f(worldPos, 1.0f).mul(mvpMatrix); // transform the point

        if (transformed.w != 0) {
            if (transformed.w < 0) {
                transformed.div((transformed.w));
                transformed.mul(-1); // hacky way to fix the artifact issues - still produces some artifacts but is much better
            } else {
                transformed.div(transformed.w); // normalize perspective divide
            }
        } else {
            System.out.println("SOMETHING IS WRONG");
        }

        // convert from clip space (-1 to 1) to screen space (0 to width/height)
        float screenX = (transformed.x + 1.0f) * 0.5f * SCREEN_WIDTH;
        float screenY = (1.0f - transformed.y) * 0.5f * SCREEN_HEIGHT; // y flipped for screen space

        return new Vector3f(screenX, screenY, transformed.z);
    }

    public void render(BufferBuilder buffer, VirtualCamera camera) {
        float halfSize = size / 2.0f;

        float[][] vertices = {
                { x - halfSize, y - halfSize, z - halfSize }, { x + halfSize, y - halfSize, z - halfSize },
                { x + halfSize, y - halfSize, z + halfSize }, { x - halfSize, y - halfSize, z + halfSize },
                { x - halfSize, y + halfSize, z - halfSize }, { x + halfSize, y + halfSize, z - halfSize },
                { x + halfSize, y + halfSize, z + halfSize }, { x - halfSize, y + halfSize, z + halfSize }
        };

        int[][] edges = {
                { 0, 1 }, { 1, 2 }, { 2, 3 }, { 3, 0 },
                { 4, 5 }, { 5, 6 }, { 6, 7 }, { 7, 4 },
                { 0, 4 }, { 1, 5 }, { 2, 6 }, { 3, 7 }
        };

        int i = 0; // i is there to just color one edge so we can maintain direction better
        for (int[] edge : edges) {
            Vector4f start = new Vector4f(vertices[edge[0]][0], vertices[edge[0]][1], vertices[edge[0]][2], 1.0f);
            Vector4f end = new Vector4f(vertices[edge[1]][0], vertices[edge[1]][1], vertices[edge[1]][2], 1.0f);

            Vector3f sProj = projectPoint(new Vector3f(start.x, start.y, start.z), camera);
            Vector3f eProj = projectPoint(new Vector3f(end.x, end.y, end.z), camera);

            if (i==4) { // color one edge
                buffer.addVertex(sProj.x, sProj.y, sProj.z)
                        .setColor(1f, 0f, 0f, 1f);
                buffer.addVertex(eProj.x, eProj.y, eProj.z)
                        .setColor(1f, 0f, 0f, 1f);
            } else {
                buffer.addVertex(sProj.x, sProj.y, sProj.z)
                        .setColor(1f, 1f, 1f, 1f);
                buffer.addVertex(eProj.x, eProj.y, eProj.z)
                        .setColor(1f, 1f, 1f, 1f);
            }
            i++;
        }
    }

}
