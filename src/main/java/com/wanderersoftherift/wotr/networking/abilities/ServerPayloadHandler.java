package com.wanderersoftherift.wotr.networking.abilities;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.Registries.AbilityRegistry;
import com.wanderersoftherift.wotr.Registries.UpgradeRegistry;
import com.wanderersoftherift.wotr.abilities.AbstractAbility;
import com.wanderersoftherift.wotr.client.gui.menu.TestMenu;
import com.wanderersoftherift.wotr.networking.data.ClaimUpgrade;
import com.wanderersoftherift.wotr.networking.data.OpenUpgradeMenu;
import com.wanderersoftherift.wotr.networking.data.UseAbility;
import com.wanderersoftherift.wotr.upgrades.AbstractUpgrade;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

public class ServerPayloadHandler {
    public static void handleAbilityOnServer(final UseAbility useAbilityPacket, final IPayloadContext context)
    {
        Optional<Registry<AbstractAbility>> reg = context.player().getServer().registryAccess().lookup(AbilityRegistry.DATA_PACK_ABILITY_REG_KEY);
        if(reg.isPresent())
        {
            if(!reg.get().get(ResourceLocation.parse(useAbilityPacket.ability_location())).isPresent())
            {
                WanderersOfTheRift.LOGGER.error("Invalid Ability Activated: " + ResourceLocation.parse(useAbilityPacket.ability_location()));
            }
            else
            {

                AbstractAbility ability = reg.get().get(ResourceLocation.parse(useAbilityPacket.ability_location())).get().value();
                if(ability.IsToggle()) // Should check last toggle, because pressing a button can send multiple packets
                {
                    if(!ability.IsToggled(context.player()))
                    {
                        ability.OnActivate(context.player());
                    }
                    else
                    {
                        ability.onDeactivate(context.player());
                    }

                    if(ability.CanPlayerUse(context.player())) ability.Toggle(context.player());
                }
                else
                {
                    ability.OnActivate(context.player());
                }
            }
        }
    }

    public static void handleUpgradeMenuOnServer(final OpenUpgradeMenu menu, final IPayloadContext context)
    {
        context.player().openMenu(new SimpleMenuProvider(
                (containerId, playerInventory, player) -> new TestMenu(containerId, playerInventory),
                Component.translatable("menu.title.examplemod.mymenu") //TODO uh change this lmao
        ));

    }

    public static void handleUpgradeOnServer(final ClaimUpgrade upgrade, final IPayloadContext context)
    {
        AbstractUpgrade abstractUpgrade = UpgradeRegistry.UPGRADE_REGISTRY.get(ResourceLocation.parse(upgrade.upgrade_location())).get().value();
        if(!abstractUpgrade.isUnlocked(context.player()))
        {
            abstractUpgrade.unlock(context.player());
        }
        else
        {
            abstractUpgrade.remove(context.player());
        }

    }
}
