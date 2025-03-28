package com.wanderersoftherift.wotr.abilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.abilities.effects.AbstractEffect;
import com.wanderersoftherift.wotr.init.ModAttachments;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.entity.EntityTypeTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * AttachedEffectData allows effects to be attached to an entity for a duration - after which they are triggered every tick until they expire.
 */
public class AttachedEffectData {
    public static final Codec<AttachedEffectData> CODEC = AttachedEffect.CODEC.listOf().xmap(AttachedEffectData::new, x -> x.effects);

    private final List<AttachedEffect> effects;

    public AttachedEffectData() {
        this(new ArrayList<>());
    }

    private AttachedEffectData(List<AttachedEffect> effects) {
        this.effects = new ArrayList<>(effects);
    }

    /**
     * Ticks all attached effects within a level
     * @param level
     */
    public static void tick(ServerLevel level) {
        level.getEntities(EntityTypeTest.forClass(LivingEntity.class), entity -> entity.hasData(ModAttachments.ATTACHED_EFFECTS)).forEach(entity -> {
            AttachedEffectData data = entity.getData(ModAttachments.ATTACHED_EFFECTS);
            data.tick(entity, level);
            if (data.effects.isEmpty()) {
                entity.removeData(ModAttachments.ATTACHED_EFFECTS);
            }
        });
    }

    public void tick(LivingEntity attachedTo, ServerLevel level) {
        effects.forEach(effect -> {
            if (level.getEntity(effect.caster) instanceof LivingEntity caster) {
                effect.childEffects.forEach(child -> child.apply(attachedTo, Collections.emptyList(), caster));
                effect.ticksRemaining--;
            } else {
                effect.ticksRemaining = 0;
            }
        });
        effects.removeIf(effect -> effect.ticksRemaining == 0);
    }

    public void attach(List<AbstractEffect> childEffects, LivingEntity caster, int ticks) {
        effects.add(new AttachedEffect(childEffects, caster.getUUID(), ticks));
    }

    private static class AttachedEffect {
        private static final Codec<AttachedEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        AbstractEffect.DIRECT_CODEC.listOf().fieldOf("childEffects").forGetter(x -> x.childEffects),
                        UUIDUtil.CODEC.fieldOf("caster").forGetter(x -> x.caster),
                        Codec.INT.fieldOf("ticksRemaining").forGetter(x -> x.ticksRemaining)
                ).apply(instance, AttachedEffect::new)
        );

        private final List<AbstractEffect> childEffects;
        private final UUID caster;
        private int ticksRemaining;

        public AttachedEffect(List<AbstractEffect> childEffects, UUID caster, int ticksRemaining) {
            this.childEffects = childEffects;
            this.caster = caster;
            this.ticksRemaining = ticksRemaining;
        }

    }
}
