package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.command.ModConfig;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.SpatialAttributeInterpolator;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnvironmentAttributeSystem.class)
public class MobSpawnSettingsMixin {

    @Inject(method = "getValue", at = @At("RETURN"), cancellable = true)
    private <Value> void starvenomore$overrideCreatureSpawnProbability(
            EnvironmentAttribute<Value> attribute, Vec3 pos, @Nullable SpatialAttributeInterpolator biomeInterpolator, CallbackInfoReturnable<Value> cir) {

        if (attribute == EnvironmentAttributes.CREATURE_WORLD_GEN_SPAWN_PROBABILITY) {
            @SuppressWarnings("unchecked")
            Value overridden = (Value) Float.valueOf(ModConfig.get().onChunkSpawnChance);
            cir.setReturnValue(overridden);
        }
    }
}
