package com.dimensiondelvers.dimensiondelvers.commands;

import com.dimensiondelvers.dimensiondelvers.gui.screen.RiftMapScreen;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class RiftMapCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(
                Commands.literal("dimensiondelvers:riftmap").executes(
                        (ctx) -> {
                            Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new RiftMapScreen(Component.literal("test"))));
                            return 1;
                        }
                )
        );
    }
}
