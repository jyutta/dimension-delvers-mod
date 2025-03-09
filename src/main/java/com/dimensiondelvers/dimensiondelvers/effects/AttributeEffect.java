package com.dimensiondelvers.dimensiondelvers.effects;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.abilities.AbilityAttributes;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class AttributeEffect extends MobEffect {
    ResourceLocation rl = DimensionDelvers.id("add_large_boost_strength_modifier");

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
