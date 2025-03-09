package com.wanderersoftherift.wotr.effects;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class AttributeEffect extends MobEffect {
    ResourceLocation rl = WanderersOfTheRift.id("add_large_boost_strength_modifier");

    public AttributeEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFF00);

//        addAttributeModifier(AbilityAttributes.LARGE_BOOST_STRENGTH, rl, 10, AttributeModifier.Operation.ADD_VALUE);
    }

//    @Override
//    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
//        if (level.isClientSide()) return true;
//        AttributeInstance attributeInstance = entity.getAttribute(AbilityAttributes.LARGE_BOOST_STRENGTH);
//        if (attributeInstance == null) return true;
//
//        attributeInstance.addTransientModifier(new AttributeModifier(rl, 10, AttributeModifier.Operation.ADD_VALUE));
//        return true;
//    }
}
