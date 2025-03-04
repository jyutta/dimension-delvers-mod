package com.dimensiondelvers.dimensiondelvers.client.map;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class contains all map data and methods for manipulating it
 * Rendering is separated into client.render package
 * Mostly empty for now, will contain player data as well
 */
public class MapData {
    public static CopyOnWriteArrayList<MapCell> cells = new CopyOnWriteArrayList<>();

    public static void addCell(MapCell cell) {
        cells.add(cell);
    }

    public static void removeCell(MapCell cell) {
        cells.remove(cell);
    }
}
