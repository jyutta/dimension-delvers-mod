package com.dimensiondelvers.dimensiondelvers.item.socket;

import com.dimensiondelvers.dimensiondelvers.init.ModDataComponentType;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.dimensiondelvers.dimensiondelvers.DimensionDelvers.MODID;
import static net.neoforged.fml.common.EventBusSubscriber.*;

@EventBusSubscriber(bus = Bus.MOD, modid = MODID)
public class GearSocketModEvents {

    @SubscribeEvent
    public static void modifyComponents(ModifyDefaultComponentsEvent event) {
        event.modify(Items.IRON_SWORD, builder ->
                builder.set(ModDataComponentType.GEAR_SOCKETS.get(), new GearSockets(getSockets()))
        );
    }

    private static @NotNull ArrayList<GearSocket> getSockets() {
        ArrayList<GearSocket> objects = new ArrayList<>();
        objects.add(new GearSocket("shapeless", null, null));
        return objects;
    }
}
