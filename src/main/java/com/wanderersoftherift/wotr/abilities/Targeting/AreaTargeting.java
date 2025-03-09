package com.wanderersoftherift.wotr.abilities.Targeting;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.abilities.AbilityAttributeHelper;
import com.wanderersoftherift.wotr.abilities.AbilityAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class AreaTargeting extends AbstractTargeting {
    private float range = 0;

    public static final MapCodec<AreaTargeting> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("range").forGetter(AreaTargeting::getRange)
            ).apply(instance, AreaTargeting::new)
    );

    public AreaTargeting(float range) {
        this.range = range;
    }

    public float getRange() {
        return range;
    }

    private float getRange(LivingEntity user) {
        return AbilityAttributeHelper.getAbilityAttribute(AbilityAttributes.AOE_SIZE, range, user);
    }

    @Override
    public MapCodec<? extends AbstractTargeting> getCodec() {
        return CODEC;
    }

    @Override
    public List<Entity> getTargetsFromEntity(Entity entity, Player caster) {
        WanderersOfTheRift.LOGGER.debug("Targeting from entity via AOE");
        float finalRange = getRange(caster);

        return entity.level().getEntities(
                entity,
                new AABB(
                        entity.position().x - (finalRange/2),
                        entity.position().y - (finalRange/2),
                        entity.position().z - (finalRange/2),
                        entity.position().x + (finalRange/2),
                        entity.position().y + (finalRange/2),
                        entity.position().z + (finalRange/2)
                ),
                (predicateEntity -> !(predicateEntity instanceof Player) && !predicateEntity.is(entity)));
    }

    @Override
    public List<Entity> getTargetsFromBlocks(List<BlockPos> blocks, Player caster) {
        WanderersOfTheRift.LOGGER.debug("Targeting from blocks via AOE");
        float finalRange = getRange(caster);

        //Gets first block and makes an area around it where the block is in the center
        //TODO look into config for selecting conditions in area since we may want to select other players for large scale heals etc
        return caster.level().getEntities(
                caster,
                new AABB(
                        blocks.get(0).getX() - (finalRange / 2),
                        blocks.get(0).getY() - (finalRange / 2),
                        blocks.get(0).getZ() - (finalRange / 2),
                        blocks.get(0).getX() + (finalRange / 2),
                        blocks.get(0).getY() + (finalRange / 2),
                        blocks.get(0).getZ() + (finalRange / 2)
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
    public List<BlockPos> getBlocksInArea(Player caster, Entity entity, List<BlockPos> targetPos) {
        WanderersOfTheRift.LOGGER.info("Targeting blocks in area via AOE");
        float finalRange = getRange(caster);

        int startX, startY, startZ;
        int endX, endY, endZ;
        List<BlockPos> blockPos = new ArrayList<>();

        //TODO maybe look into java ranges?
        if(entity == null) {

            BlockPos first = targetPos.get(0);
            startX = (int) (first.getX() - (finalRange/2));
            startY = (int) (first.getY() - (finalRange/2));
            startZ = (int) (first.getZ() - (finalRange/2));

            endX = (int) (first.getX() + (finalRange/2));
            endY = (int) (first.getY() + (finalRange/2));
            endZ = (int) (first.getZ() + (finalRange/2));
        }
        else {
            //TODO make this use player block position rather than absolute pos since it causes rounding issues converting to int.
            startX = (int) (entity.getX() - (finalRange/2));
            startY = (int) (entity.getY() - (finalRange/2));
            startZ = (int) (entity.getZ() - (finalRange/2));

            endX = (int) (entity.getX() + (finalRange/2));
            endY = (int) (entity.getY() + (finalRange/2));
            endZ = (int) (entity.getZ() + (finalRange/2));
        }

        for(int x = startX ; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                for (int z = startZ; z < endZ; z++) {
                    if (!caster.level().getBlockState(new BlockPos(x, y, z)).isAir()) {
                        blockPos.add(new BlockPos(x, y, z));
                    }
                }
            }
        }

        return blockPos;
    }
}
