package com.dipazio.dpsvarmod.register;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public class DPsFoodsEffects {
    public static final Consumable COMBINED_ROTTEN_FLESH = Consumables.defaultFood().onConsume(
            new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 1200, 2), 1.0F)).build();
}
