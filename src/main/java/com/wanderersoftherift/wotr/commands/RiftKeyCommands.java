package com.wanderersoftherift.wotr.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.init.ModDataComponentType;
import com.wanderersoftherift.wotr.init.ModItems;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

public class RiftKeyCommands extends BaseCommand{
    public RiftKeyCommands() {
        super("riftKey", Commands.LEVEL_GAMEMASTERS);
    }

    @Override protected void buildCommand(LiteralArgumentBuilder<CommandSourceStack> builder, CommandBuildContext context) {
        builder.then(Commands.literal("tier")
            .then(Commands.argument("tier", IntegerArgumentType.integer())
                .executes(this::setTier)
            )
        );
        builder.then(Commands.literal("theme")
            .then(Commands.argument("theme", ResourceLocationArgument.id())
                .suggests((ctx, sb) ->
                    SharedSuggestionProvider.suggestResource(
                        Set.of(WanderersOfTheRift.id("cave"), WanderersOfTheRift.id("forest"))
                        , sb))
                .executes(this::setTheme)
            )
        );
        builder.then(Commands.literal("seed")
            .then(Commands.argument("seed", IntegerArgumentType.integer())
                .executes(this::setSeed)
            )
        );
    }

    private int setTier(CommandContext<CommandSourceStack> stack) {
        ServerPlayer player = stack.getSource().getPlayer();

        if (player != null) {
            ItemStack heldItem = player.getMainHandItem();

            if(heldItem.getItem() != ModItems.RIFT_KEY.asItem()) {
                stack.getSource().sendFailure(Component.translatable("command.dimensiondelvers.get_item_stack_components.invalid_item"));
                return 0;
            }

            heldItem.set(ModDataComponentType.RIFT_TIER, stack.getArgument("tier", Integer.class));
            return 1;
        }

        stack.getSource().sendFailure(Component.translatable("command.dimensiondelvers.get_item_stack_components.invalid_player"));
        return 0;
    }

    private int setTheme(CommandContext<CommandSourceStack> stack) {
        ServerPlayer player = stack.getSource().getPlayer();

        if (player != null) {
            ItemStack heldItem = player.getMainHandItem();

            if(heldItem.getItem() != ModItems.RIFT_KEY.asItem()) {
                stack.getSource().sendFailure(Component.translatable("command.dimensiondelvers.get_item_stack_components.invalid_item"));
                return 0;
            }

            heldItem.set(ModDataComponentType.RIFT_THEME, ResourceLocationArgument.getId(stack, "theme"));
            return 1;
        }

        stack.getSource().sendFailure(Component.translatable("command.dimensiondelvers.get_item_stack_components.invalid_player"));
        return 0;
    }

    private int setSeed(CommandContext<CommandSourceStack> stack) {
        ServerPlayer player = stack.getSource().getPlayer();

        if (player != null) {
            ItemStack heldItem = player.getMainHandItem();

            if(heldItem.getItem() != ModItems.RIFT_KEY.asItem()) {
                stack.getSource().sendFailure(Component.translatable("command.dimensiondelvers.get_item_stack_components.invalid_item"));
                return 0;
            }

            heldItem.set(ModDataComponentType.RIFT_SEED, stack.getArgument("seed", Integer.class));
            return 1;
        }

        stack.getSource().sendFailure(Component.translatable("command.dimensiondelvers.get_item_stack_components.invalid_player"));
        return 0;
    }
}
