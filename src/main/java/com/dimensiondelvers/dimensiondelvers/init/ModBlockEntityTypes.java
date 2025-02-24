package com.dimensiondelvers.dimensiondelvers.init;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.block.entity.DittoBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
import java.util.function.Supplier;

public class ModBlockEntityTypes {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
			DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, DimensionDelvers.MODID);

	public static final Supplier<BlockEntityType<DittoBlockEntity>> DITTO_BLOCK_ENTITY = BLOCK_ENTITIES.register(
			"ditto_block_entity",
			// The block entity type.
			() -> new BlockEntityType<>(
					DittoBlockEntity::new,
					Set.of(
							ModBlocks.DITTO_BLOCK.get(),
							ModBlocks.TRAP_BLOCK.get(),
							ModBlocks.MOB_TRAP_BLOCK.get(),
							ModBlocks.PLAYER_TRAP_BLOCK.get(),
							ModBlocks.SPRING_BLOCK.get()
					)
			)
	);
}
