package com.dimensiondelvers.dimensiondelvers.client.map;

import org.joml.Vector3f;

import java.util.ArrayList;

/**
 * Represents a room on the map
 * Mainly used to make rendering work well without spaghetti code
 * Contains the list of cells that make up the room and are the building block of map,
 * as well as the position and metadata of the room
 */
public class MapRoom {
    public int x, y, z;
    public int sizeX, sizeY, sizeZ; // sizes for the room cuboid !! don't forget to remove .1 from north and east when rendering to make space for potential tunnels - maybe even make the size configurable
    private final float TWEEN_TUNNEL_SIZE = 0.1f; // size of the tunnel between rooms - gets subtracted from room size when rendering
    public Vector3f pos1, pos2;
    public ArrayList<MapCell> cells;

    // to solve the rendering the 1wide tunnels, on render, go through all the cells that have the possibility of having tunnel and check their variable
    // TODO: move rendering from MapCell to here, basically rewrite MapCell
}
