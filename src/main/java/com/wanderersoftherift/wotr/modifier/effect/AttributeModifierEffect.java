package com.wanderersoftherift.wotr.modifier.effect;

import com.google.common.collect.HashMultimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.client.tooltip.ImageComponent;
import com.wanderersoftherift.wotr.modifier.source.ModifierSource;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class AttributeModifierEffect extends AbstractModifierEffect {
    public static final MapCodec<AttributeModifierEffect> MODIFIER_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(AttributeModifierEffect::getId),
                    Attribute.CODEC.fieldOf("attribute").forGetter(AttributeModifierEffect::getAttribute),
                    Codec.FLOAT.fieldOf("min_roll").forGetter(AttributeModifierEffect::getMinimumRoll),
                    Codec.FLOAT.fieldOf("max_roll").forGetter(AttributeModifierEffect::getMaximumRoll),
                    AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(AttributeModifierEffect::getOperation)
            ).apply(instance, AttributeModifierEffect::new)
    );

    @Override
    public MapCodec<? extends AbstractModifierEffect> getCodec() {
        return MODIFIER_CODEC;
    }

    private final ResourceLocation id;
    private final Holder<Attribute> attribute;
    private final float minRoll;
    private final float maxRoll;
    private final AttributeModifier.Operation operation;

    public AttributeModifierEffect(ResourceLocation id, Holder<Attribute> attribute, float minRoll, float maxRoll, AttributeModifier.Operation operation) {
        this.id = id;
        this.attribute = attribute;
        this.minRoll = minRoll;
        this.maxRoll = maxRoll;
        this.operation = operation;
    }

    public ResourceLocation getId() {
        return id;
    }

    public Holder<Attribute> getAttribute(){
        return attribute;
    }

    public float getMinimumRoll(){
        return minRoll;
    }

    public float getMaximumRoll(){
        return maxRoll;
    }

    public AttributeModifier.Operation getOperation() {
        return this.operation;
    }

    private ResourceLocation idForSlot(StringRepresentable source) {
        return this.id.withSuffix("/" + source.getSerializedName());
    }

    public AttributeModifier getModifier(float roll, StringRepresentable source) {
        return new AttributeModifier(this.idForSlot(source), calculateModifier(roll) , this.getOperation());
    }

    public double calculateModifier(float roll) {
        return (roll * (maxRoll - minRoll)) + minRoll;
    }

    @Override
    public void enableModifier(float roll, Entity entity, ModifierSource source) {
        if (entity instanceof LivingEntity livingentity) {
            livingentity.getAttributes().addTransientAttributeModifiers(this.makeAttributeMap(roll, source));
        }
    }

    @Override
    public void disableModifier(float roll, Entity entity, ModifierSource source) {
        if (entity instanceof LivingEntity livingentity) {
            livingentity.getAttributes().removeAttributeModifiers(this.makeAttributeMap(roll, source));
        }
    }

    @Override
    public void applyModifier() {
        // NOOP
    }

    private HashMultimap<Holder<Attribute>, AttributeModifier> makeAttributeMap(float roll, ModifierSource source) {
        HashMultimap<Holder<Attribute>, AttributeModifier> hashmultimap = HashMultimap.create();
        hashmultimap.put(this.attribute, this.getModifier(roll, source));
        return hashmultimap;
    }

    @Override
    public TooltipComponent getTooltipComponent(ItemStack stack, float roll, ChatFormatting chatFormatting) {
        return switch (this.getOperation()) {
            case ADD_VALUE -> getAddTooltipComponent(stack, roll, chatFormatting);
            case ADD_MULTIPLIED_BASE, ADD_MULTIPLIED_TOTAL -> getMultiplyTooltipComponent(stack, roll, chatFormatting);
        };
    }

    private TooltipComponent getAddTooltipComponent(ItemStack stack, float roll, ChatFormatting chatFormatting) {
        double calculatedRoll = calculateModifier(roll);
        float roundedValue = (float) (Math.ceil(calculatedRoll * 100) / 100);
        String sign = (roundedValue > 0) ? "positive" : "negative";

        MutableComponent cmp = Component.translatable("modifier."+ WanderersOfTheRift.MODID + ".attribute.add." +sign, roundedValue, Component.translatable(attribute.value().getDescriptionId())).withStyle(chatFormatting);
        return new ImageComponent(stack, cmp, WanderersOfTheRift.id("textures/tooltip/attribute/damage_attribute.png"));
    }

    private TooltipComponent getMultiplyTooltipComponent(ItemStack stack, float roll, ChatFormatting chatFormatting) {
        double calculatedRoll = calculateModifier(roll);
        int roundedValue = (int) Math.ceil(calculatedRoll * 100);
        String sign = (roundedValue > 0) ? "positive" : "negative";

        MutableComponent cmp = Component.translatable("modifier."+ WanderersOfTheRift.MODID + ".attribute.multiply."+sign, roundedValue, Component.translatable(attribute.value().getDescriptionId())).withStyle(chatFormatting);
        return new ImageComponent(stack, cmp, WanderersOfTheRift.id("textures/tooltip/attribute/damage_attribute.png"));
    }
}
