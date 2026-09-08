package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.StarveNoMore;
import net.damushken.starve_no_more.command.ModConfig;
import net.damushken.starve_no_more.util.ModTags;
import net.damushken.starve_no_more.util.PlumpAccess;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PassiveEntity.class)
public abstract class PassiveEntityMixin implements PlumpAccess {

    @Unique
    private static final TrackedData<Boolean> STARVENOMORE_PLUMP =
            DataTracker.registerData(PassiveEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Unique
    private static final TrackedData<Boolean> STARVENOMORE_PLAYER_LINEAGE =
            DataTracker.registerData(PassiveEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Unique
    private float starvenomore$growthAccumulator = 0f;
    @Unique
    private int starvenomore$ticksSinceBreed = 0;

    @Unique
    private DataTracker starvenomore$tracker() {
        return ((Entity)(Object) this).getDataTracker();
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void starvenomore$initPlumpTracker(CallbackInfo ci) {
        starvenomore$tracker().startTracking(STARVENOMORE_PLUMP, false);
        starvenomore$tracker().startTracking(STARVENOMORE_PLAYER_LINEAGE, false);
    }

    @Override
    public boolean starvenomore$isPlayerLineage() {
        return starvenomore$tracker().get(STARVENOMORE_PLAYER_LINEAGE);
    }

    @Override
    public void starvenomore$setPlayerLineage(boolean value) {
        starvenomore$tracker().set(STARVENOMORE_PLAYER_LINEAGE, value);
    }

    @Override
    public boolean starvenomore$isPlump() {
        return starvenomore$tracker().get(STARVENOMORE_PLUMP);
    }

    @Override
    public void starvenomore$setPlump(boolean plump) {
        starvenomore$tracker().set(STARVENOMORE_PLUMP, plump);
    }

    @Override
    public void starvenomore$resetBreedTimer() {
        this.starvenomore$ticksSinceBreed = 0;
        this.starvenomore$setPlump(false);
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void starvenomore$onTickMovement(CallbackInfo ci) {
        PassiveEntity self = (PassiveEntity)(Object) this;
        if (self.getWorld().isClient) return;

        ModConfig cfg = ModConfig.get();

        // HAYBALE GROWTH
        if (self.isBaby() && cfg.doOnHaybaleFasterBabyGrowth) {
            BlockPos below = self.getBlockPos().down();
            if (self.getWorld().getBlockState(below).getBlock() == Blocks.HAY_BLOCK) {
                if (self.getWorld() instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER,
                            self.getX(), self.getY() + 0.5, self.getZ(),
                            1, 0.3, 0.3, 0.3, 0.0);
                }
                starvenomore$growthAccumulator += cfg.onHaybaleFasterBabyGrowthMultiplier / 100f;
                while (starvenomore$growthAccumulator >= 1f) {
                    starvenomore$growthAccumulator -= 1f;
                    int age = self.getBreedingAge();
                    if (age < 0) self.setBreedingAge(Math.min(age + 1, 0));
                }
            }
        }

        // PLUMP TRACKING
        if (self.isBaby() || starvenomore$isPlump()) return;
        if (!self.getType().isIn(ModTags.EntityTypes.CAN_PLUMP)) return;
        if (!cfg.doPlump) return;
        if (self instanceof GoatEntity && !cfg.doGoatsDropAndPlump) return;
        if (!cfg.doWildPlump && !starvenomore$isPlayerLineage()) return;

        starvenomore$ticksSinceBreed++;
        int thresholdTicks = cfg.plumpDays * 24000;
        if (starvenomore$ticksSinceBreed >= thresholdTicks) {
            starvenomore$setPlump(true);

            if (self.getWorld() instanceof ServerWorld serverWorld) {

                serverWorld.playSound(null, self.getX(), self.getY(), self.getZ(),
                        SoundEvents.BLOCK_FUNGUS_BREAK, self.getSoundCategory(),
                        5.0f, 0.5f);

                serverWorld.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        self.getX(), self.getY() + 0.2, self.getZ(),
                        16, 0.7, 0.1, 0.7, 0);



                serverWorld.spawnParticles(ParticleTypes.EFFECT,
                        self.getX(), self.getY() + 0.5, self.getZ(),
                        24, 0.4, 1.0, 0.4, 0.05);
            }

        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void starvenomore$writePlump(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("StarveNoMoreTicksSinceBreed", starvenomore$ticksSinceBreed);
        nbt.putBoolean("StarveNoMorePlump", starvenomore$isPlump());
        nbt.putBoolean("StarveNoMorePlayerLineage", starvenomore$isPlayerLineage());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void starvenomore$readPlump(NbtCompound nbt, CallbackInfo ci) {
        starvenomore$ticksSinceBreed = nbt.getInt("StarveNoMoreTicksSinceBreed");
        starvenomore$setPlump(nbt.getBoolean("StarveNoMorePlump"));
        starvenomore$setPlayerLineage(nbt.getBoolean("StarveNoMorePlayerLineage"));
    }
}
