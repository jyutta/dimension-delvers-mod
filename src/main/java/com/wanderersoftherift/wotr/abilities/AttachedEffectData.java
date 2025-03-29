package com.wanderersoftherift.wotr.abilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.abilities.effects.AbstractEffect;
import com.wanderersoftherift.wotr.abilities.effects.marker.EffectMarker;
import com.wanderersoftherift.wotr.init.ModAttachments;
import com.wanderersoftherift.wotr.init.RegistryEvents;
import com.wanderersoftherift.wotr.network.SetEffectMarkerPayload;
import com.wanderersoftherift.wotr.network.UpdateEffectMarkersPayload;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import java.util.Objects;
import java.util.OptionalInt;
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
                if (effect.display == null || updates.containsKey(effect.display)) {
                    continue;
                }
                int remainingDuration = getRemainingDuration(effect.display);
                if (remainingDuration > 0) {
                    updates.put(effect.display, remainingDuration);
                } else {
                    remove.add(effect.display);
                }
            }
            PacketDistributor.sendToPlayer(player, new UpdateEffectMarkersPayload(updates, remove));
        }
    }

    private int getRemainingDuration(Holder<EffectMarker> display) {
        OptionalInt max = effects.stream().filter(x -> Objects.equals(display, x.display)).mapToInt(x -> x.ticksRemaining).max();
        return max.orElse(0);
    }

    private @NotNull List<AttachedEffect> tickEffects(LivingEntity attachedTo, ServerLevel level) {
        List<AttachedEffect> removedEffects = new ArrayList<>();
        Iterator<AttachedEffect> iterator = effects.iterator();
        while (iterator.hasNext()) {
            AttachedEffect effect = iterator.next();
            LivingEntity caster = effect.getCaster(level);
            if (caster != null) {
                effect.childEffects.forEach(child -> child.apply(attachedTo, Collections.emptyList(), caster));
                effect.ticksRemaining--;
            } else {
                effect.ticksRemaining = 0;
            }
            if (effect.ticksRemaining == 0) {
                removedEffects.add(effect);
                iterator.remove();
            }
        }
        return removedEffects;
    }

    public void attach(LivingEntity attachedTo, List<AbstractEffect> childEffects, LivingEntity caster, int ticks, Holder<EffectMarker> marker) {
        if (attachedTo instanceof ServerPlayer player
                && marker != null
                && effects.stream().noneMatch(x -> Objects.equals(x.display, marker) && x.ticksRemaining > ticks)) {
            PacketDistributor.sendToPlayer(player, new SetEffectMarkerPayload(marker, ticks));
        }
        effects.add(new AttachedEffect(childEffects, caster.getUUID(), ticks, marker));
    }

    public Map<Holder<EffectMarker>, Integer> getDisplayData() {
        Map<Holder<EffectMarker>, Integer> result = new LinkedHashMap<>();
        for (AttachedEffect effect : effects) {
            if (effect.display != null) {
                if (!result.containsKey(effect.display) || result.get(effect.display) < effect.ticksRemaining) {
                    result.put(effect.display, effect.ticksRemaining);
                }
            }
        }
        return result;
    }

    private static class AttachedEffect {
        private static final Codec<AttachedEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        AbstractEffect.DIRECT_CODEC.listOf().fieldOf("childEffects").forGetter(x -> x.childEffects),
                        UUIDUtil.CODEC.fieldOf("caster").forGetter(x -> x.caster),
                        Codec.INT.fieldOf("ticksRemaining").forGetter(x -> x.ticksRemaining),
                        RegistryFixedCodec.create(RegistryEvents.EFFECT_MARKER_REGISTRY).optionalFieldOf("display", null).forGetter(x -> x.display)
                ).apply(instance, AttachedEffect::new)
        );

        private final List<AbstractEffect> childEffects;
        private final UUID caster;
        private LivingEntity cachedCaster;
        private final Holder<EffectMarker> display;
        private int ticksRemaining;

        public AttachedEffect(List<AbstractEffect> childEffects, UUID caster, int ticksRemaining, Holder<EffectMarker> display) {
            this.childEffects = childEffects;
            this.caster = caster;
            this.ticksRemaining = ticksRemaining;
            this.display = display;
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
    }
}
