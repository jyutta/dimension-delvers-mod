package com.wanderersoftherift.wotr.abilities;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AbilityAttributes {

    /*
     * ------THINGS HERE------
     * CDR
     * PER SKILL COOLDOWNS (that way each cooldown amount can differ per person and be modified)
     * MAX MANA
     * Pretty much any stat we want to modify on the player
     *
     * ----THINGS NOT HERE!----
     * Things that change often, and not controlled a ton such as actual cooldown tracking, active mana amount, etc
     */

    //TODO Replace these with our custom modifier system
    public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(Registries.ATTRIBUTE, WanderersOfTheRift.MODID);

    public static final DeferredHolder<Attribute, RangedAttribute> MAX_MANA = REGISTRY.register("max_mana", () -> new RangedAttribute("attribute." + WanderersOfTheRift.MODID + ".max_mana", 100, 0, 100));
    public static final DeferredHolder<Attribute, RangedAttribute> HEAL_POWER = REGISTRY.register("heal_power", () -> new RangedAttribute("attribute." + WanderersOfTheRift.MODID + ".heal_amount", 3, 0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, RangedAttribute> COOLDOWN = REGISTRY.register("cooldown", () -> new RangedAttribute("attribute." + WanderersOfTheRift.MODID + ".cooldown", 1, 0, Integer.MAX_VALUE));

    //TODO these can be stages from the level up system to apply different rates or multipliers to the different skills
//    public static final AttributeModifier HEAL_MODIFIER = new AttributeModifier(WanderersOfTheRift.id("increase_max_heal"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    public static final AttributeModifier BOOST_COOLDOWN_MODIFIER = new AttributeModifier(WanderersOfTheRift.id("reduce_boost_cooldown"), -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
//    public static final AttributeModifier SMOL_MODIFIER = new AttributeModifier(WanderersOfTheRift.id("make_smol"), -0.75, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);


}
