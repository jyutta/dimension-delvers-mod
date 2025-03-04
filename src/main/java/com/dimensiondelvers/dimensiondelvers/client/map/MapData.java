package com.dimensiondelvers.dimensiondelvers.client.map;

import org.joml.Vector3i;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class contains all map data and methods for manipulating it
 * Rendering is separated into client.render package
 * Mostly empty for now, will contain player data as well
 */
public class MapData {
    public static HashMap<Vector3i, MapCell> cells = new HashMap<>();

    /**
     * Used to add new cell to the map
     * In the future will be used to process cell changes like the small 1wide tunnels between rooms
     * @param cell
     */
    public static void addCell(MapCell cell) {
        cells.put(new Vector3i(cell.x, cell.y, cell.z), cell);
    }

    public static void removeCell(MapCell cell) {
        cells.remove(new Vector3i(cell.x, cell.y, cell.z));
    }
}
