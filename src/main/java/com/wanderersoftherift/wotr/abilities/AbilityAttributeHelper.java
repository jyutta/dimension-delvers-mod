package com.wanderersoftherift.wotr.abilities;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class AbilityAttributeHelper {
    public static float getAbilityAttribute(Holder<Attribute> attributeHolder, float baseValue, LivingEntity user) {
        AttributeInstance attribute = user.getAttribute(attributeHolder);
        if (attribute != null) {
            AttributeModifier modifier = new AttributeModifier(WanderersOfTheRift.id("base_value"), baseValue, AttributeModifier.Operation.ADD_VALUE);
            attribute.addTransientModifier(modifier);
            float value = (float) attribute.getValue();
            attribute.removeModifier(modifier);
            return value;
        }
        return 0;
    }
}
