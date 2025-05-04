package com.wanderersoftherift.wotr.block;

import com.mojang.serialization.MapCodec;
import com.wanderersoftherift.wotr.block.blockentity.RiftMobSpawnerBlockEntity;
import com.wanderersoftherift.wotr.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Spawner;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import javax.annotation.Nullable;
import java.util.List;

public class RiftMobSpawnerBlock extends BaseEntityBlock {
    public static final MapCodec<RiftMobSpawnerBlock> CODEC = simpleCodec(RiftMobSpawnerBlock::new);
    public static final EnumProperty<TrialSpawnerState> STATE = BlockStateProperties.TRIAL_SPAWNER_STATE;
    public static final BooleanProperty OMINOUS = BlockStateProperties.OMINOUS;

    @Override
    public MapCodec<RiftMobSpawnerBlock> codec() {
        return CODEC;
    }

    public RiftMobSpawnerBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STATE, TrialSpawnerState.INACTIVE).setValue(OMINOUS, Boolean.valueOf(false)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_312785_) {
        p_312785_.add(STATE, OMINOUS);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new RiftMobSpawnerBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level instanceof ServerLevel serverlevel
                ? createTickerHelper(
                blockEntityType,
                ModBlockEntities.RIFT_MOB_SPAWNER.get(),
                (p_337976_, p_337977_, p_337978_, p_337979_) -> p_337979_.getTrialSpawner()
                        .tickServer(serverlevel, p_337977_, p_337978_.getOptionalValue(BlockStateProperties.OMINOUS).orElse(false))
        )
                : createTickerHelper(
                blockEntityType,
                ModBlockEntities.RIFT_MOB_SPAWNER.get(),
                (p_337980_, p_337981_, p_337982_, p_337983_) -> p_337983_.getTrialSpawner()
                        .tickClient(p_337980_, p_337981_, p_337982_.getOptionalValue(BlockStateProperties.OMINOUS).orElse(false))
        );
    }

    @Override
    public void appendHoverText(ItemStack p_312446_, Item.TooltipContext p_339621_, List<Component> p_312088_, TooltipFlag p_311895_) {
        super.appendHoverText(p_312446_, p_339621_, p_312088_, p_311895_);
        Spawner.appendHoverText(p_312446_, p_312088_, "spawn_data");
    }
}
