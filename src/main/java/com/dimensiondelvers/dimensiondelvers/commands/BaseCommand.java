package com.dimensiondelvers.dimensiondelvers.commands;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;


/**
 * Abstract base class for all commands in the mod.
 * Provides common functionality for command registration and permission handling.
 */
public abstract class BaseCommand {

    /**
     * Registers this command with the provided dispatcher.
     * The command is automatically added under the mod's namespace.
     *
     * @param dispatcher The command dispatcher handling command registration.
     * @param context    The command build context (used for parameter parsing).
     */
    public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        // Define the base command with permission level
        LiteralArgumentBuilder<CommandSourceStack> argumentBuilder = Commands.literal(getName())
                .requires(sender -> sender.hasPermission(getPermissionLevel()));

        this.buildCommand(argumentBuilder, context); // Build subcommands
        dispatcher.register(Commands.literal(DimensionDelvers.MODID).then(argumentBuilder)); // Register under the mod's namespace
    }

    /**
     * Allows subclasses to define subcommands or additional arguments.
     *
     * @param builder The command builder to which subcommands should be added.
     */
    protected abstract void buildCommand(LiteralArgumentBuilder<CommandSourceStack> builder, CommandBuildContext context);
    protected abstract String getName(); // This defines the subcommand name used after the mod's namespace.

    /**
     * Determines the permission level required to execute this command.<br>
     *
     * Permission Levels:<br>
     * - `0` → All players<br>
     * - `1` → Moderator<br>
     * - `2` → Game Master (Recommended for most mod-related commands)<br>
     * - `3` → Admin<br>
     * - `4` → Server Owner<br>
     *
     * @return The required permission level for this command.
     * @see <a href="https://minecraft.wiki/w/Permission_level#Java_Edition">Java Edition: Permission Levels</a>
     */
    protected abstract int getPermissionLevel();
}
