package com.wanderersoftherift.wotr.gui.layer.objective;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.rift.objective.OngoingObjective;
import com.wanderersoftherift.wotr.rift.objective.ProgressObjective;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class GeneralObjectiveBarRenderer extends ObjectiveRenderer {
    private static final ResourceLocation BAR_BACKGROUND_SPRITE = WanderersOfTheRift
            .id("objective/general/bar_background");
    private static final ResourceLocation BAR_PROGRESS_SPRITE = WanderersOfTheRift.id("objective/general/bar_progress");

    private static final Component OBJECTIVE_COMPLETE = Component
            .translatable(WanderersOfTheRift.translationId("gui", "objective_status.complete"));

    private final ProgressObjective objective;

    public GeneralObjectiveBarRenderer(ProgressObjective objective) {
        this.objective = objective;
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (objective.isComplete()) {
            Font font = Minecraft.getInstance().font;
            guiGraphics.drawString(font, OBJECTIVE_COMPLETE,
                    guiGraphics.guiWidth() / 2 - font.width(OBJECTIVE_COMPLETE) / 2, 24,
                    ChatFormatting.WHITE.getColor());
        } else {
            int textureWidth = 102;
            int x = (guiGraphics.guiWidth() / 2) - (textureWidth / 2);
            int y = 24;
            float progress = (float) objective.getCurrentProgress() / objective.getTargetProgress();
            int progressWidth = (int) (progress * (textureWidth + 1));
            guiGraphics.blitSprite(RenderType::guiTextured, BAR_BACKGROUND_SPRITE, x, y, textureWidth, 5);
            if (progressWidth > 0) {
                guiGraphics.blitSprite(RenderType::guiTextured, BAR_PROGRESS_SPRITE, textureWidth, 5, 0, 0, x, y,
                        progressWidth, 5);
            }
        }
    }

    public static GeneralObjectiveBarRenderer create(OngoingObjective objective) {
        if (objective instanceof ProgressObjective progressObjective) {
            return new GeneralObjectiveBarRenderer(progressObjective);
        }
        return null;
    }
}
