package com.wanderersoftherift.wotr.item.implicit;

import com.wanderersoftherift.wotr.init.ModDataComponentType;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

import static com.wanderersoftherift.wotr.WanderersOfTheRift.MODID;
import static net.neoforged.fml.common.EventBusSubscriber.Bus;

@EventBusSubscriber(bus = Bus.MOD, modid = MODID)
public class GearImplicitsModEvents {

    @SubscribeEvent
    public static void modifyComponents(ModifyDefaultComponentsEvent event) {
        event.modify(Items.WOODEN_SWORD,
                builder -> builder.set(ModDataComponentType.GEAR_IMPLICITS.get(), new UnrolledGearImplicits()));
        event.modify(Items.STONE_SWORD,
                builder -> builder.set(ModDataComponentType.GEAR_IMPLICITS.get(), new UnrolledGearImplicits()));
        event.modify(Items.IRON_SWORD,
                builder -> builder.set(ModDataComponentType.GEAR_IMPLICITS.get(), new UnrolledGearImplicits()));
        event.modify(Items.GOLDEN_SWORD,
                builder -> builder.set(ModDataComponentType.GEAR_IMPLICITS.get(), new UnrolledGearImplicits()));
        event.modify(Items.DIAMOND_SWORD,
                builder -> builder.set(ModDataComponentType.GEAR_IMPLICITS.get(), new UnrolledGearImplicits()));
        event.modify(Items.NETHERITE_SWORD,
                builder -> builder.set(ModDataComponentType.GEAR_IMPLICITS.get(), new UnrolledGearImplicits()));
        event.modify(Items.WOODEN_AXE,
                builder -> builder.set(ModDataComponentType.GEAR_IMPLICITS.get(), new UnrolledGearImplicits()));
        event.modify(Items.STONE_AXE,
                builder -> builder.set(ModDataComponentType.GEAR_IMPLICITS.get(), new UnrolledGearImplicits()));
        event.modify(Items.IRON_AXE,
                builder -> builder.set(ModDataComponentType.GEAR_IMPLICITS.get(), new UnrolledGearImplicits()));
        event.modify(Items.GOLDEN_AXE,
                builder -> builder.set(ModDataComponentType.GEAR_IMPLICITS.get(), new UnrolledGearImplicits()));
        event.modify(Items.DIAMOND_AXE,
                builder -> builder.set(ModDataComponentType.GEAR_IMPLICITS.get(), new UnrolledGearImplicits()));
        event.modify(Items.NETHERITE_AXE,
                builder -> builder.set(ModDataComponentType.GEAR_IMPLICITS.get(), new UnrolledGearImplicits()));
    }
}
