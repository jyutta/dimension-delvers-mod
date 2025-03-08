package com.dimensiondelvers.dimensiondelvers.client.map;

import com.mojang.blaze3d.vertex.BufferBuilder;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;

import static com.dimensiondelvers.dimensiondelvers.client.map.Utils3D.calculateVertices;
import static com.dimensiondelvers.dimensiondelvers.client.map.Utils3D.projectPoint;

/**
 * Represents a room on the map
 * Mainly used to make rendering work well without spaghetti code
 * Contains the list of cells that make up the room and are the building block of map,
 * as well as the position and metadata of the room
 */
public class MapRoom {
    public int x, y, z;
    public int sizeX, sizeY, sizeZ; // sizes for the room cuboid !! don't forget to remove .1 from north and east when rendering to make space for potential tunnels - maybe even make the size configurable
    private final float TWEEN_TUNNEL_SIZE = 0.3f; // size of the tunnel between rooms - gets subtracted from room size when rendering
    public Vector3f pos1, pos2;
    public ArrayList<MapCell> cells = new ArrayList<>();

    // to solve the rendering the 1wide tunnels, on render, go through all the cells that have the possibility of having tunnel and check their variable
    // TODO: move rendering from MapCell to here, basically rewrite MapCell

    public MapRoom(int x, int y, int z, int sizeX, int sizeY, int sizeZ, ArrayList<MapCell> cells) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.pos1 = new Vector3f(x, y, z);
        this.pos2 = new Vector3f(x + sizeX, y + sizeY, z + sizeZ);
        if (cells != null) this.cells.addAll(cells);
    }

    private final int[][] wireEdges = {
            { 0, 1 }, { 1, 2 }, { 2, 3 }, { 3, 0 },
            { 4, 5 }, { 5, 6 }, { 6, 7 }, { 7, 4 },
            { 0, 4 }, { 1, 5 }, { 2, 6 }, { 3, 7 }
    };

    /**
     * Adds the cube to the buffer for rendering, assumes DEBUG_LINES renderer is active
     * @param buffer
     * @param camera
     */
    public void renderWireframe(BufferBuilder buffer, com.dimensiondelvers.dimensiondelvers.client.map.VirtualCamera camera, Vector2i mapPosition, Vector2i mapSize) {
        Vector3f pos2_sub = new Vector3f(pos2.x - TWEEN_TUNNEL_SIZE, pos2.y - TWEEN_TUNNEL_SIZE, pos2.z - TWEEN_TUNNEL_SIZE);
        float[][] vertices = calculateVertices(pos1, pos2_sub);

        int i = 0; // i is there to just color one edge so we can maintain direction better
        for (int[] edge : wireEdges) {
            Vector3f sProj = projectPoint(vertices[edge[0]][0], vertices[edge[0]][1], vertices[edge[0]][2], camera, mapPosition, mapSize);
            Vector3f eProj = projectPoint(vertices[edge[1]][0], vertices[edge[1]][1], vertices[edge[1]][2], camera, mapPosition, mapSize);

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

    private final int [][] cubeFaces = {
            {0, 1, 2, 3}, // bottom
            {7, 6, 5, 4}, // top
            {4, 5, 1, 0}, // front
            {6, 7, 3, 2}, // back
            {0, 3, 7, 4}, // left
            {5, 6, 2, 1}  // right
    };

    /**
     * Adds the cube to the buffer for rendering, assumes QUADS renderer is active
     * @param buffer
     * @param camera
     */
    public void renderCube(BufferBuilder buffer, VirtualCamera camera, Vector4f color, Vector2i mapPosition, Vector2i mapSize) {
        Vector3f pos2_sub = new Vector3f(pos2.x - TWEEN_TUNNEL_SIZE, pos2.y - TWEEN_TUNNEL_SIZE, pos2.z - TWEEN_TUNNEL_SIZE);
        float[][] vertices = calculateVertices(pos1, pos2_sub);

        for (int[] face : cubeFaces) {
            Vector3f p1 = projectPoint(vertices[face[0]][0], vertices[face[0]][1], vertices[face[0]][2], camera, mapPosition, mapSize);
            Vector3f p2 = projectPoint(vertices[face[1]][0], vertices[face[1]][1], vertices[face[1]][2], camera, mapPosition, mapSize);
            Vector3f p3 = projectPoint(vertices[face[2]][0], vertices[face[2]][1], vertices[face[2]][2], camera, mapPosition, mapSize);
            Vector3f p4 = projectPoint(vertices[face[3]][0], vertices[face[3]][1], vertices[face[3]][2], camera, mapPosition, mapSize);

            buffer.addVertex(p1.x, p1.y, p1.z).setColor(color.x, color.y, color.z, color.w);
            buffer.addVertex(p2.x, p2.y, p2.z).setColor(color.x, color.y, color.z, color.w);
            buffer.addVertex(p3.x, p3.y, p3.z).setColor(color.x, color.y, color.z, color.w);
            buffer.addVertex(p4.x, p4.y, p4.z).setColor(color.x, color.y, color.z, color.w);
        }
    }

}
