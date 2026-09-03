package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.command.ModConfig;
import net.minecraft.world.biome.SpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnSettings.class)
public class SpawnSettingsMixin {

    @Inject(method = "getCreatureSpawnProbability", at = @At("RETURN"), cancellable = true)
    private void starvenomore$overrideProbability(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(ModConfig.get().onChunkSpawnChance);
    }
}
