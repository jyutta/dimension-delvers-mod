package com.wanderersoftherift.wotr.abilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.abilities.effects.AbstractEffect;
import com.wanderersoftherift.wotr.abilities.effects.AttachEffect;
import com.wanderersoftherift.wotr.abilities.effects.marker.EffectMarker;
import com.wanderersoftherift.wotr.abilities.effects.predicate.ContinueEffectPredicate;
import com.wanderersoftherift.wotr.abilities.effects.predicate.TriggerPredicate;
import com.wanderersoftherift.wotr.init.ModAttachments;
import com.wanderersoftherift.wotr.init.RegistryEvents;
import com.wanderersoftherift.wotr.network.SetEffectMarkerPayload;
import com.wanderersoftherift.wotr.network.UpdateEffectMarkersPayload;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * AttachedEffectData allows effects to be attached to an entity for a durationTicks - after which they are triggered every tick until they expire.
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
     *
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
        List<AttachedEffect> removedEffects = tickEffects(attachedTo, level);

        if (attachedTo instanceof ServerPlayer player) {
            Map<Holder<EffectMarker>, Integer> updates = new LinkedHashMap<>();
            List<Holder<EffectMarker>> remove = new ArrayList<>();
            for (AttachedEffect effect : removedEffects) {
                effect.display.ifPresent(display -> {
                    if (!updates.containsKey(display) && !remove.contains(display)) {
                        int remainingDuration = getRemainingDuration(display);
                        if (remainingDuration > 0) {
                            updates.put(display, remainingDuration);
                        } else {
                            remove.add(display);
                        }
                    }
                });
            }
            PacketDistributor.sendToPlayer(player, new UpdateEffectMarkersPayload(updates, remove));
        }
    }

    private int getRemainingDuration(Holder<EffectMarker> display) {
        return effects.stream().filter(x -> display.equals(x.display.orElse(null))).mapToInt(AttachedEffect::getRemainingDuration).max().orElse(0);
    }

    private @NotNull List<AttachedEffect> tickEffects(LivingEntity attachedTo, ServerLevel level) {
        List<AttachedEffect> removedEffects = new ArrayList<>();
        Iterator<AttachedEffect> iterator = effects.iterator();
        while (iterator.hasNext()) {
            AttachedEffect effect = iterator.next();
            if (effect.tick(attachedTo, level)) {
                removedEffects.add(effect);
                iterator.remove();
            }
        }
        return removedEffects;
    }

    public void attach(LivingEntity attachedTo, LivingEntity caster, AttachEffect attachEffect) {
        AttachedEffect newEffect = new AttachedEffect(attachEffect.getEffects(), attachEffect.getTriggerPredicate(), attachEffect.getContinuePredicate(), attachEffect.getDisplay(), caster);
        if (attachedTo instanceof ServerPlayer player
                && attachEffect.getDisplay() != null) {
            int existingDuration = getRemainingDuration(attachEffect.getDisplay());
            int newDuration = newEffect.getRemainingDuration();
            if (existingDuration < newDuration) {
                PacketDistributor.sendToPlayer(player, new SetEffectMarkerPayload(attachEffect.getDisplay(), newDuration));
            }
        }
        effects.add(newEffect);
    }

    public Map<Holder<EffectMarker>, Integer> getDisplayData() {
        Map<Holder<EffectMarker>, Integer> result = new LinkedHashMap<>();
        for (AttachedEffect effect : effects) {
            effect.display.ifPresent(display -> {
                if (!result.containsKey(display) || result.get(display) < effect.getRemainingDuration()) {
                    result.put(display, effect.getRemainingDuration());
                }
            });
        }
        return result;
    }

    private static class AttachedEffect {
        private static final Codec<AttachedEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        AbstractEffect.DIRECT_CODEC.listOf().fieldOf("childEffects").forGetter(x -> x.childEffects),
                        TriggerPredicate.CODEC.fieldOf("triggerPredicate").forGetter(x -> x.triggerPredicate),
                        ContinueEffectPredicate.CODEC.fieldOf("continuePredicate").forGetter(x -> x.continuePredicate),
                        RegistryFixedCodec.create(RegistryEvents.EFFECT_MARKER_REGISTRY).optionalFieldOf("display").forGetter(x -> x.display),
                        UUIDUtil.CODEC.fieldOf("caster").forGetter(x -> x.caster),
                        Codec.INT.fieldOf("triggeredTimes").forGetter(x -> x.triggeredTimes),
                        Codec.INT.fieldOf("ticks").forGetter(x -> x.ticks)
                ).apply(instance, AttachedEffect::new)
        );

        private final List<AbstractEffect> childEffects;
        private final TriggerPredicate triggerPredicate;
        private final ContinueEffectPredicate continuePredicate;
        private final Optional<Holder<EffectMarker>> display;

        private final UUID caster;
        private int triggeredTimes;
        private int ticks;

        private LivingEntity cachedCaster;

        public AttachedEffect(List<AbstractEffect> childEffects, TriggerPredicate triggerPredicate, ContinueEffectPredicate continuePredicate, Optional<Holder<EffectMarker>> display, UUID caster, int triggeredTimes, int ticks) {
            this.childEffects = childEffects;
            this.caster = caster;
            this.triggerPredicate = triggerPredicate;
            this.continuePredicate = continuePredicate;
            this.display = display;
            this.triggeredTimes = triggeredTimes;
            this.ticks = ticks;
        }

        public AttachedEffect(List<AbstractEffect> childEffects, TriggerPredicate triggerPredicate, ContinueEffectPredicate continuePredicate, Holder<EffectMarker> display, LivingEntity caster) {
            this(childEffects, triggerPredicate, continuePredicate, Optional.ofNullable(display), caster.getUUID(), 0, 0);
            this.cachedCaster = caster;
        }

        /**
         * Ticks this attached effect
         * @param attachedTo The entity it is attached to
         * @param level The level it is within
         * @return Whether to remove this effect
         */
        public boolean tick(Entity attachedTo, ServerLevel level) {
            LivingEntity caster = getCaster(level);
            if (caster == null) {
                return true;
            }
            if (triggerPredicate.matches(attachedTo, ticks, caster)) {
                childEffects.forEach(child -> child.apply(attachedTo, Collections.emptyList(), caster));
                triggeredTimes++;
            }
            ticks++;
            return !continuePredicate.matches(attachedTo, ticks, triggeredTimes, caster);
        }

        public LivingEntity getCaster(ServerLevel level) {
            if (cachedCaster != null) {
                return cachedCaster.isRemoved() ? null : cachedCaster;
            }
            if (level.getEntity(caster) instanceof LivingEntity casterEntity) {
                cachedCaster = casterEntity;
            }
            return cachedCaster;
        }

        public int getRemainingDuration() {
            if (continuePredicate.duration() == 0) {
                return Integer.MAX_VALUE;
            }
            return continuePredicate.duration() - ticks;
        }

    }
}
