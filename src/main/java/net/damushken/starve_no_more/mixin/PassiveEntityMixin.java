package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.StarveNoMore;
import net.damushken.starve_no_more.command.ModConfig;
import net.minecraft.block.Blocks;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PassiveEntity.class)
public abstract class PassiveEntityMixin {

    @Unique
    private float starvenomore$growthAccumulator = 0f;

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void starvenomore$onHaybaleGrowth(CallbackInfo ci) {
        PassiveEntity self = (PassiveEntity)(Object) this;
        if (self.getWorld().isClient) return;
        if (!self.isBaby()) return;

        ModConfig cfg = ModConfig.get();
        if (!cfg.doOnHaybaleFasterBabyGrowth) return;

        BlockPos below = self.getBlockPos().down();
        if (self.getWorld().getBlockState(below).getBlock() != Blocks.HAY_BLOCK) return;

        StarveNoMore.LOGGER.info("Baby {} on hay, age={}, acc={}",
                self.getType(), self.getBreedingAge(), starvenomore$growthAccumulator);

        if (self.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    ParticleTypes.HAPPY_VILLAGER,
                    self.getX(), self.getY() + 0.5, self.getZ(),
                    1,
                    0.3, 0.3, 0.3,
                    0.0
            );
        }

        starvenomore$growthAccumulator += cfg.onHaybaleFasterBabyGrowthMultiplier / 100f;

        while (starvenomore$growthAccumulator >= 1f) {
            starvenomore$growthAccumulator -= 1f;
            int age = self.getBreedingAge();
            if (age < 0) {
                self.setBreedingAge(Math.min(age + 1, 0));
                StarveNoMore.LOGGER.info("Bonus tick applied, new age={}", self.getBreedingAge());
            }
        }
    }
}
