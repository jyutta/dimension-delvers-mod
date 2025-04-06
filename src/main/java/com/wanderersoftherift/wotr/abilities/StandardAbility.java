package com.wanderersoftherift.wotr.abilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.abilities.effects.AbstractEffect;
import com.wanderersoftherift.wotr.abilities.effects.EffectContext;
import com.wanderersoftherift.wotr.init.ModAttributes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StandardAbility extends AbstractAbility {


    @Override
    public MapCodec<? extends AbstractAbility> getCodec() {
        return CODEC;
    }

    public static final MapCodec<StandardAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("ability_name").forGetter(StandardAbility::getName),
                    ResourceLocation.CODEC.fieldOf("icon").forGetter(StandardAbility::getIcon),
                    Codec.INT.fieldOf("cooldown").forGetter(ability -> (int) ability.getBaseCooldown()),
                    Codec.list(AbstractEffect.DIRECT_CODEC).optionalFieldOf("effects", Collections.emptyList()).forGetter(AbstractAbility::getEffects)
            ).apply(instance, StandardAbility::new)
    );

    public StandardAbility(ResourceLocation resourceLocation, ResourceLocation icon, int baseCooldown, List<AbstractEffect> effects) {
        super(resourceLocation, icon, effects);
        this.baseCooldown = baseCooldown;
    }

    @Override
    public void onActivate(Player player, int slot, ItemStack abilityItem) {
        if (this.canPlayerUse(player)) {
            if (!this.isOnCooldown(player, slot)) {
                EffectContext effectContext = new EffectContext(player, abilityItem);
                effectContext.enableModifiers();
                try {
                    this.getEffects().forEach(effect -> effect.apply(player, new ArrayList<>(), effectContext));
                    this.setCooldown(player, slot, effectContext.getAbilityAttribute(ModAttributes.COOLDOWN, baseCooldown));
                } finally {
                    effectContext.disableModifiers();
                }
            }
        }

        //this is an example of handing a case where the player cannot use an ability
        if (!this.canPlayerUse(player)) {
            ((ServerPlayer) player).sendSystemMessage(Component.literal("You cannot use this"));
        }
    }


    @Override
    public void onDeactivate(Player player, int slot) {

    }

    @Override
    public void tick(Player player) {

    }
}
