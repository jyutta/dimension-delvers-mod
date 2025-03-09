package com.wanderersoftherift.wotr.abilities.Targeting;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class AreaTargeting extends AbstractTargeting {
    private double range = 0;

    public static final MapCodec<AreaTargeting> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.DOUBLE.fieldOf("range").forGetter(AreaTargeting::getRange)
            ).apply(instance, AreaTargeting::new)
    );

    public AreaTargeting(double range) {
        this.range = range;
    }

    public double getRange() {
        return range;
    }

    @Override
    public MapCodec<? extends AbstractTargeting> getCodec() {
        return CODEC;
    }

    @Override
    public List<Entity> getTargetsFromEntity(Entity entity) {
        WanderersOfTheRift.LOGGER.debug("Targeting from entity via AOE");

        return entity.level().getEntities(
                entity,
                new AABB(
                        entity.position().x - (range/2),
                        entity.position().y - (range/2),
                        entity.position().z - (range/2),
                        entity.position().x + (range/2),
                        entity.position().y + (range/2),
                        entity.position().z + (range/2)
                ),
                (predicateEntity -> !(predicateEntity instanceof Player) && !predicateEntity.is(entity)));
    }

    @Override
    public List<Entity> getTargetsFromBlocks(List<BlockPos> blocks, Player caster) {
        WanderersOfTheRift.LOGGER.debug("Targeting from blocks via AOE");

        //Gets first block and makes an area around it where the block is in the center
        //TODO look into config for selecting conditions in area since we may want to select other players for large scale heals etc
        return caster.level().getEntities(
                caster,
                new AABB(
                        blocks.get(0).getX() - (range / 2),
                        blocks.get(0).getY() - (range / 2),
                        blocks.get(0).getZ() - (range / 2),
                        blocks.get(0).getX() + (range / 2),
                        blocks.get(0).getY() + (range / 2),
                        blocks.get(0).getZ() + (range / 2)
                ),
                (entity -> !(entity instanceof Player) && !entity.is(caster)));
    }

//    @Override
//    public List<BlockPos> getBlocks(Entity entity) {
//        WanderersOfTheRift.LOGGER.info("Targeting AOE");
//         TODO think about handling all blocks in an AOE?
//         return entity.level().getEntities(user, new AABB(user.position().x - (range/2), user.position().y - (range/2), user.position().z - (range/2), user.position().x + (range/2), user.position().y + (range/2), user.position().z + (range/2)), (entity -> !(entity instanceof Player) && !entity.is(user)));
//    }


    @Override
    public List<BlockPos> getBlocksInArea(Level level, Entity entity, List<BlockPos> targetPos) {
        WanderersOfTheRift.LOGGER.info("Targeting blocks in area via AOE");

        int startX, startY, startZ;
        int endX, endY, endZ;
        List<BlockPos> blockPos = new ArrayList<>();

        //TODO maybe look into java ranges?
        if(entity == null) {

            BlockPos first = targetPos.get(0);
            startX = (int) (first.getX() - (range/2));
            startY = (int) (first.getY() - (range/2));
            startZ = (int) (first.getZ() - (range/2));

            endX = (int) (first.getX() + (range/2));
            endY = (int) (first.getY() + (range/2));
            endZ = (int) (first.getZ() + (range/2));
        }
        else {
            //TODO make this use player block position rather than absolute pos since it causes rounding issues converting to int.
            startX = (int) (entity.getX() - (range/2));
            startY = (int) (entity.getY() - (range/2));
            startZ = (int) (entity.getZ() - (range/2));

            endX = (int) (entity.getX() + (range/2));
            endY = (int) (entity.getY() + (range/2));
            endZ = (int) (entity.getZ() + (range/2));
        }

        for(int x = startX ; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                for (int z = startZ; z < endZ; z++) {
                    if (!level.getBlockState(new BlockPos(x, y, z)).isAir()) {
                        blockPos.add(new BlockPos(x, y, z));
                    }
                }
            }
        }

        return blockPos;
    }
}
