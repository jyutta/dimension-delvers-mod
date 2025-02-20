package com.dimensiondelvers.dimensiondelvers.riftmap;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.joml.Vector2i;
import org.joml.Vector3d;

import java.util.ArrayList;

@EventBusSubscriber(modid = "dimensiondelvers", value = Dist.CLIENT)
public class RiftMap {
    public static ArrayList<MapCell> cells = new ArrayList<>();
    public static Vector2i mapPosition = new Vector2i(0, 0);
    public static Vector2i mapSize = new Vector2i(Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight());
    private static Vector2i scissorCoords = new Vector2i(0, 0);
    private static Vector2i scissorSize = new Vector2i(0, 0);

    private static VirtualCamera camera = new VirtualCamera(70.0f, 16f/9f, 0.1f, 1000.0f);

    public static VirtualCamera getCamera() {
        return camera;
    }

    public static void addCell(MapCell cell) {
        cells.add(cell);
    }

    public static void removeCell(MapCell cell) {
        cells.remove(cell);
    }

    /**
     * Renders the map - duh
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void renderMap(RenderGuiEvent.Post event) {
        setMapSize(0,0, 100, 100); // set the position and size of the map, will not be here in prod, here for testing
        
        // bunch of RenderSystem stuff, don't touch, it's radioactive and extremely volatile
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();

        // !!! Scissor coords are from bottom left and not scaled with gui scale !!! the below code renders top left quadrant
        // the RenderSystem is from top left instead because yes
        RenderSystem.enableScissor(scissorCoords.x, scissorCoords.y, scissorSize.x, scissorSize.y);

        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR); // prep the buffer
        
        RenderSystem.setShader(CoreShaders.POSITION_COLOR);

        drawOutline(buffer); // draw *debug* outlines around the map

        // just some testing cubes to render
        Cube cube1 = new Cube(new Vector3d(0, 0, 0), new Vector3d(1, -2, 1));
        Cube cube2 = new Cube(new Vector3d(2,0,0), new Vector3d(3,2,1));// 2,0,0
        Cube cube3 = new Cube(new Vector3d(-2, -2, 2), new Vector3d(-1, -1, 3)); //-2,-2,2

        Cube player = new Cube(new Vector3d(0.25f, 0.25f, 0.25f), new Vector3d(0.75f, 0.75f, 0.75f));
        cube1.render(buffer, camera);
        cube2.render(buffer, camera);
        cube3.render(buffer, camera);

        player.render(buffer, camera);

        // sets both position and rotation
        camera.orbitAroundOrigin(Minecraft.getInstance().player.getViewXRot(1.0f), -Minecraft.getInstance().player.getViewYRot(1.0f), 10.0f);

        // prepare the buffer for rendering and draw it
        MeshData bufferData = buffer.build();
        if (bufferData != null) {
            BufferUploader.drawWithShader(bufferData);
        }
        RenderSystem.disableScissor();
        RenderSystem.disableBlend();
    }

    /**
     * Sets the position and size of the map and calculates the proper scissor coords and size + virtual camera aspect ratio
     * Uses the usual top left corner origin coords and width, height (unlike scissor)
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public static void setMapSize(int x, int y, int width, int height) {
        mapPosition.x = x;
        mapPosition.y = y;
        mapSize.x = width;
        mapSize.y = height;

        // calculate coefficients for scaling
        float widthCoef = (float) Minecraft.getInstance().getWindow().getWidth() / Minecraft.getInstance().getWindow().getGuiScaledWidth();
        float heightCoef = (float) Minecraft.getInstance().getWindow().getHeight() / Minecraft.getInstance().getWindow().getGuiScaledHeight();

        // calculate the scissor coords - pos is from top left, scissors are from bottom left
        scissorCoords.x = x;
        scissorCoords.y = (int) (Minecraft.getInstance().getWindow().getHeight()-(y+height)*heightCoef);

        // calculate the scissor size - width, height are scaled to gui, scissor is not so it has to be scaled back
        scissorSize.x = (int) (width * widthCoef);
        scissorSize.y = (int) (height * heightCoef);

        // update aspect ratio of the virtual camera
        camera.setAspectRatio((float) width / height);
    }

    /**
     * Draws the outline of the map - is not good gets culled without manual +-1s around, don't use for the actual implementation
     * @param buffer
     */
    private static void drawOutline(BufferBuilder buffer) {
        buffer.addVertex(mapPosition.x, mapPosition.y, 0.0f).setColor(255, 255, 255, 255);
        buffer.addVertex(mapPosition.x + mapSize.x, mapPosition.y, 0.0f).setColor(255, 255, 255, 255);

        buffer.addVertex(mapPosition.x + mapSize.x, mapPosition.y, 0.0f).setColor(255, 255, 255, 255);
        buffer.addVertex(mapPosition.x + mapSize.x, mapPosition.y + mapSize.y-1, 0.0f).setColor(255, 255, 255, 255);

        buffer.addVertex(mapPosition.x + mapSize.x, mapPosition.y + mapSize.y-1, 0.0f).setColor(255, 255, 255, 255);
        buffer.addVertex(mapPosition.x + 1, mapPosition.y + mapSize.y-1, 0.0f).setColor(255, 255, 255, 255);

        buffer.addVertex(mapPosition.x+1, mapPosition.y + mapSize.y-1, 0.0f).setColor(255, 255, 255, 255);
        buffer.addVertex(mapPosition.x+1, mapPosition.y-1, 0.0f).setColor(255, 255, 255, 255);
    }
}
