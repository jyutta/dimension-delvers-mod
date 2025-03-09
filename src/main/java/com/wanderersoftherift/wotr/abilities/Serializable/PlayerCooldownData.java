package com.wanderersoftherift.wotr.abilities.Serializable;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.abilities.AbstractAbility;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class PlayerCooldownData extends SerializableMap {


    public void setCooldown(ResourceLocation loc, int amount) {
        this.insert(loc, amount);
    }

    public int getCooldown(ResourceLocation loc)
    {
        return this.get(loc);
    }

    public boolean isOnCooldown(ResourceLocation loc)
    {
        return this.containsKey(loc) && this.get(loc) > 0;
    }

    public void reduceCooldowns()
    {
        reduceAll(1);
    }

    public List<AbstractAbility> getActiveCooldowns(Registry<AbstractAbility> abilityRegistry)
    {
        List<AbstractAbility> onCoolDown = new ArrayList<>();
        for(ResourceLocation loc: this.getKeys())
        {
            if(abilityRegistry.get(loc).isPresent())
            {
                onCoolDown.add(abilityRegistry.get(loc).get().value());
            }
        }

        return onCoolDown;
    }
}
