package com.wanderersoftherift.wotr.abilities;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.abilities.upgrade.UpgradePool;
import com.wanderersoftherift.wotr.init.ModDataComponentType;
import com.wanderersoftherift.wotr.modifier.ModifierHelper;
import com.wanderersoftherift.wotr.modifier.source.AbilityUpgradeModifierSource;
import com.wanderersoftherift.wotr.modifier.source.ModifierSource;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public record EffectContext(LivingEntity caster, ItemStack abilityItem) {

    public Level level() {
        return caster.level();
    }

    public AbstractAbility getAbility() {
        Holder<AbstractAbility> holder = abilityItem.get(ModDataComponentType.ABILITY);
        if (holder != null) {
            return holder.value();
        }
        return null;
    }

    public void enableModifiers() {
        if (caster != null && !caster.isRemoved()) {
            ModifierHelper.enableModifier(caster);
        }
        UpgradePool pool = abilityItem.get(ModDataComponentType.UPGRADE_POOL);
        if (pool != null) {
            pool.forEachSelected((upgrade, selection) -> {
                ModifierSource source = new AbilityUpgradeModifierSource(selection);
                upgrade.modifierEffects().forEach(effect -> effect.enableModifier(0, caster, source));
            });
        }
    }

    public void disableModifiers() {
        if (caster != null && !caster.isRemoved()) {
            ModifierHelper.disableModifier(caster);
        }
        UpgradePool pool = abilityItem.get(ModDataComponentType.UPGRADE_POOL);
        if (pool != null) {
            pool.forEachSelected((upgrade, selection) -> {
                ModifierSource source = new AbilityUpgradeModifierSource(selection);
                upgrade.modifierEffects().forEach(effect -> effect.disableModifier(0, caster, source));
            });
        }
    }

    public float getAbilityAttribute(Holder<Attribute> attributeHolder, float baseValue) {
        AttributeInstance attribute = caster.getAttribute(attributeHolder);
        if (attribute == null) {
            return 0;
        }

        AttributeModifier baseModifier = new AttributeModifier(WanderersOfTheRift.id("base_value"), baseValue, AttributeModifier.Operation.ADD_VALUE);
        attribute.addTransientModifier(baseModifier);
        float value = (float) attribute.getValue();
        attribute.removeModifier(baseModifier);
        return value;
    }
}
