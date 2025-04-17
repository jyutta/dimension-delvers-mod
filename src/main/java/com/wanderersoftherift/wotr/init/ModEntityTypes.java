package com.wanderersoftherift.wotr.init;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.entity.portal.RiftPortalEntranceEntity;
import com.wanderersoftherift.wotr.entity.portal.RiftPortalExitEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntityTypes {
    public static final DeferredRegister.Entities ENTITIES = DeferredRegister.createEntities(WanderersOfTheRift.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<RiftPortalEntranceEntity>> RIFT_ENTRANCE = ENTITIES.registerEntityType(
            "rift_entrance",
            RiftPortalEntranceEntity::new,
            MobCategory.MISC,
            builder -> builder.sized(0.5f, 3f));
    public static final DeferredHolder<EntityType<?>, EntityType<RiftPortalExitEntity>> RIFT_EXIT = ENTITIES.registerEntityType(
            "rift_exit",
            RiftPortalExitEntity::new,
            MobCategory.MISC,
            builder -> builder.sized(0.5f, 3f));

}
