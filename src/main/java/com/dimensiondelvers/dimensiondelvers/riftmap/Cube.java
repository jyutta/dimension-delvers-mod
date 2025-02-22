package com.dimensiondelvers.dimensiondelvers.riftmap;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.Minecraft;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Cube class for RiftMap 3D renderer
 * Holds the position and size of a cube and contains the render method used to draw it
 */
public class Cube {
    private Vector3d point1 = new Vector3d(0,0,0);
    private Vector3d point2 = new Vector3d(0,0,0);

    public Cube(Vector3d point1, Vector3d point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    /**
     * Calculates the screen position of a 3D point
     * @param worldPos
     * @param camera
     * @return
     */
    private static Vector3f projectPoint(Vector3f worldPos, VirtualCamera camera) {
        int mapWidth = RiftMap.mapSize.x; // this is the width and height of the window we want to draw
        int mapHeight = RiftMap.mapSize.y;

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

        // convert from clip space (-1 to 1) to screen space (0 to width/height) and apply position offset
        float screenX = RiftMap.mapPosition.x + (transformed.x + 1.0f) * 0.5f * mapWidth;
        float screenY = RiftMap.mapPosition.y + (1.0f - transformed.y) * 0.5f * mapHeight; // y flipped for screen space

        return new Vector3f(screenX, screenY, -transformed.z);
    }

    private float[][] calculateVertices() {

        float minX = (float) Math.min(point1.x, point2.x);
        float maxX = (float) Math.max(point1.x, point2.x);
        float minY = (float) Math.min(point1.y, point2.y);
        float maxY = (float) Math.max(point1.y, point2.y);
        float minZ = (float) Math.min(point1.z, point2.z);
        float maxZ = (float) Math.max(point1.z, point2.z);

        return new float[][]{
            {minX, minY, minZ}, {maxX, minY, minZ},
                {maxX, minY, maxZ}, {minX, minY, maxZ},
                {minX, maxY, minZ}, {maxX, maxY, minZ},
                {maxX, maxY, maxZ}, {minX, maxY, maxZ}
        };
    }

    /**
     * Adds the cube to the buffer for rendering, assumes DEBUG_LINES renderer is active
     * @param buffer
     * @param camera
     */
    public void renderWireframe(BufferBuilder buffer, VirtualCamera camera) {
        float[][] vertices = calculateVertices();

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

    /**
     * Adds the cube to the buffer for rendering, assumes QUADS renderer is active
     * @param buffer
     * @param camera
     */
    public void renderCube(BufferBuilder buffer, VirtualCamera camera) {
        float[][] vertices = calculateVertices();

        int[][] faces = {
                {0, 1, 2, 3}, // bottom
                {4, 5, 6, 7}, // top
                {0, 1, 5, 4}, // front
                {2, 3, 7, 6}, // back
                {0, 3, 7, 4}, // left
                {1, 2, 6, 5}  // right
        };

        for (int[] face : faces) {
            Vector4f v1 = new Vector4f(vertices[face[0]][0], vertices[face[0]][1], vertices[face[0]][2], 1.0f);
            Vector4f v2 = new Vector4f(vertices[face[1]][0], vertices[face[1]][1], vertices[face[1]][2], 1.0f);
            Vector4f v3 = new Vector4f(vertices[face[2]][0], vertices[face[2]][1], vertices[face[2]][2], 1.0f);
            Vector4f v4 = new Vector4f(vertices[face[3]][0], vertices[face[3]][1], vertices[face[3]][2], 1.0f);

            Vector3f p1 = projectPoint(new Vector3f(v1.x, v1.y, v1.z), camera);
            Vector3f p2 = projectPoint(new Vector3f(v2.x, v2.y, v2.z), camera);
            Vector3f p3 = projectPoint(new Vector3f(v3.x, v3.y, v3.z), camera);
            Vector3f p4 = projectPoint(new Vector3f(v4.x, v4.y, v4.z), camera);

            buffer.addVertex(p1.x, p1.y, p1.z).setColor(0f, 0f, 1.0f, 1.0f);
            buffer.addVertex(p2.x, p2.y, p2.z).setColor(0f, 0f, 1.0f, 1.0f);
            buffer.addVertex(p3.x, p3.y, p3.z).setColor(0f, 0f, 1.0f, 1.0f);
            buffer.addVertex(p4.x, p4.y, p4.z).setColor(0f, 0f, 1.0f, 1.0f);
        }
    }

}
