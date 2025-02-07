package com.dimensiondelvers.dimensiondelvers.riftmap;

import com.dimensiondelvers.dimensiondelvers.events.RenderEvents;
import com.google.common.eventbus.Subscribe;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.GameRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

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

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        buffer.addVertex(1.0f, 1.0f, 0f).setColor(1f, 1f, 1f, 1f);
        buffer.addVertex(1.0f, 5.0f, 0f).setColor(1f, 1f, 1f, 1f);
        buffer.addVertex(1.0f, 5.0f, 0f).setColor(1f, 1f, 1f, 1f);
        buffer.addVertex(5.0f, 5.0f, 0f).setColor(1f, 1f, 1f, 1f);
        buffer.addVertex(5.0f, 5.0f, 0f).setColor(1f, 1f, 1f, 1f);
        buffer.addVertex(5.0f, 1.0f, 0f).setColor(1f, 1f, 1f, 1f);
        buffer.addVertex(5.0f, 1.0f, 0f).setColor(1f, 1f, 1f, 1f);
        buffer.addVertex(1.0f, 1.0f, 0f).setColor(1f, 1f, 1f, 1f);


        BufferUploader.drawWithShader(buffer.buildOrThrow());
        RenderSystem.disableBlend();
    }
}
