package com.dimensiondelvers.dimensiondelvers.riftmap;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.Minecraft;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.*;
import java.util.Arrays;

// contains vertexes for a cube
// construction accepts the x,y,z of the cube and the size of the cube
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
        int SCREEN_WIDTH = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int SCREEN_HEIGHT = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        Matrix4f mvpMatrix = new Matrix4f()
                .set(camera.getProjectionMatrix())  // Apply Projection
                .mul(camera.getViewMatrix());       // Apply View

        Vector4f transformed = new Vector4f(worldPos, 1.0f).mul(mvpMatrix); // Transform point


        if (transformed.w != 0) {
            transformed.div(transformed.w); // Normalize perspective divide
        } else {
            System.out.println("SOMETHING IS WRONG");
        }

        // Convert from clip space (-1 to 1) to screen space (0 to width/height)
        float screenX = (transformed.x + 1.0f) * 0.5f * SCREEN_WIDTH;
        float screenY = (1.0f - transformed.y) * 0.5f * SCREEN_HEIGHT; // Flip Y for screen space

        if (screenX > 0.5f*SCREEN_WIDTH) {
            System.out.println("screenX: " + screenX);
            System.out.println("screenY: " + screenY);
        }

        return new Vector3f(screenX, screenY, transformed.z);
    }

    public void render(BufferBuilder buffer, VirtualCamera camera) {

        float x_offset = Minecraft.getInstance().getWindow().getGuiScaledWidth()/2.0f;
        float y_offset = Minecraft.getInstance().getWindow().getGuiScaledHeight()/2.0f;

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

        //Matrix4f projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(45.0), 1f, 0.1f, 100.0f); //16/9
        //System.out.println("render");
        //System.out.println(Arrays.deepToString(vertices));


        for (int[] edge : edges) {
            Vector4f start = new Vector4f(vertices[edge[0]][0], vertices[edge[0]][1], vertices[edge[0]][2], 1.0f);
            Vector4f end = new Vector4f(vertices[edge[1]][0], vertices[edge[1]][1], vertices[edge[1]][2], 1.0f);

            //start.mul(projectionMatrix);
            //end.mul(projectionMatrix);

            Vector3f sProj = projectPoint(new Vector3f(start.x, start.y, start.z), camera);
            Vector3f eProj = projectPoint(new Vector3f(end.x, end.y, end.z), camera);

            if (sProj == null || eProj == null) {
                continue; // Skip lines outside the screen
            }

            //System.out.println(sProj);
            //System.out.println(eProj);


            float f = 50;
            float x1 = (f*start.x)/start.z;
            float y1 = (f*start.y)/start.z;
            float x2 = (f*end.x)/end.z;
            float y2 = (f*end.y)/end.z;

            /*System.out.println("start");
            System.out.println(start.x/start.w);
            System.out.println(start.y/start.w);
            System.out.println(start.z/start.w);
            System.out.println(end.x/end.w);
            System.out.println(end.y/end.w);
            System.out.println(end.z/end.w);*/



            buffer.addVertex(sProj.x, sProj.y, sProj.z)
                    .setColor(1f, 1f, 1f, 1f);
            buffer.addVertex(eProj.x, eProj.y, eProj.z)
                    .setColor(1f, 1f, 1f, 1f);
            /*buffer.addVertex(start.x / start.w + x_offset, start.y / start.w + y_offset, start.z / start.w)
                    .setColor(1f, 1f, 1f, 1f);
            buffer.addVertex(end.x / end.w + x_offset, end.y / end.w + y_offset, end.z / end.w)
                    .setColor(1f, 1f, 1f, 1f);*/
        }
    }

}
