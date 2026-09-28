package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.StarveNoMore;
import net.damushken.starve_no_more.command.ModConfig;
import net.damushken.starve_no_more.util.ModTags;
import net.damushken.starve_no_more.util.PlumpAccess;
import net.damushken.starve_no_more.util.PlumpUtil;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.animal.equine.SkeletonHorse;
import net.minecraft.world.entity.animal.equine.ZombieHorse;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.SpellParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AgeableMob.class)
public abstract class AgeableMobMixin implements PlumpAccess {

    @Unique
    private static final EntityDataAccessor<Boolean> STARVENOMORE_PLUMP =
            SynchedEntityData.defineId(AgeableMob.class, EntityDataSerializers.BOOLEAN);

    @Unique
    private static final EntityDataAccessor<Boolean> STARVENOMORE_PLAYER_LINEAGE =
            SynchedEntityData.defineId(AgeableMob.class, EntityDataSerializers.BOOLEAN);

    @Unique
    private static final EntityDataAccessor<Boolean> STARVENOMORE_EFFECTIVE_PLUMP_SYNCED =
            SynchedEntityData.defineId(AgeableMob.class, EntityDataSerializers.BOOLEAN);

    @Unique
    private static final EntityDataAccessor<Float> STARVENOMORE_SYNCED_SCALE =
            SynchedEntityData.defineId(AgeableMob.class, EntityDataSerializers.FLOAT);

    @Unique
    private float starvenomore$growthAccumulator = 0f;
    @Unique
    private int starvenomore$ticksSinceBreed = 0;

    @Unique
    private SynchedEntityData starvenomore$tracker() {
        return ((Entity)(Object) this).getEntityData();
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void starvenomore$initPlumpTracker(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(STARVENOMORE_PLUMP, false);
        builder.define(STARVENOMORE_PLAYER_LINEAGE, false);
        builder.define(STARVENOMORE_EFFECTIVE_PLUMP_SYNCED, false);
        builder.define(STARVENOMORE_SYNCED_SCALE, 1.25f);
    }

    @Override
    public boolean starvenomore$isEffectivelyPlumpSynced() {
        return starvenomore$tracker().get(STARVENOMORE_EFFECTIVE_PLUMP_SYNCED);
    }

    @Override
    public void starvenomore$setEffectivelyPlumpSynced(boolean value) {
        starvenomore$tracker().set(STARVENOMORE_EFFECTIVE_PLUMP_SYNCED, value);
    }

    @Override
    public float starvenomore$getSyncedScale() {
        return starvenomore$tracker().get(STARVENOMORE_SYNCED_SCALE);
    }

    @Override
    public void starvenomore$setSyncedScale(float scale) {
        starvenomore$tracker().set(STARVENOMORE_SYNCED_SCALE, scale);
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

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void starvenomore$onTickMovement(CallbackInfo ci) {
        AgeableMob self = (AgeableMob)(Object) this;
        if (self.level() instanceof ClientLevel) return;

        ModConfig cfg = ModConfig.get();

        // HAYBALE GROWTH
        if (self.isBaby() && cfg.doOnHaybaleFasterBabyGrowth) {
            BlockPos below = self.blockPosition().below();
            if (self.level().getBlockState(below).getBlock() == Blocks.HAY_BLOCK) {
                if (self.level() instanceof ServerLevel serverWorld) {
                    serverWorld.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                            self.getX(), self.getY() + 0.5, self.getZ(),
                            1, 0.3, 0.3, 0.3, 0.0);
                }
                starvenomore$growthAccumulator += cfg.onHaybaleFasterBabyGrowthMultiplier / 100f;
                while (starvenomore$growthAccumulator >= 1f) {
                    starvenomore$growthAccumulator -= 1f;
                    int age = self.getAge();
                    if (age < 0) self.setAge(Math.min(age + 1, 0));
                }
            }
        }

        // Recompute + sync effective plump state every tick
        // This lets clients (especially on dedicated servers, where ModConfig
        // is not directly readable) always show the correct plump visuals
        // without needing config access themselves.
        boolean effective = PlumpUtil.isEffectivelyPlump(self);
        if (starvenomore$isEffectivelyPlumpSynced() != effective) {
            starvenomore$setEffectivelyPlumpSynced(effective);
        }
        if (effective) {
            float currentScale = cfg.plumpScale;
            if (starvenomore$getSyncedScale() != currentScale) {
                starvenomore$setSyncedScale(currentScale);
            }
        }

        // PLUMP TRACKING
        if (self.isBaby() || starvenomore$isPlump()) return;
        if (!self.is(ModTags.EntityTypes.CAN_PLUMP)) return;
        if (!cfg.doPlump) return;
        if (self instanceof Goat && !cfg.doGoatsDropAndPlump) return;
        if (!cfg.doWildPlump && !starvenomore$isPlayerLineage()) return;

        starvenomore$ticksSinceBreed++;
        int thresholdTicks = cfg.plumpDays * 20; //24000
        if (starvenomore$ticksSinceBreed >= thresholdTicks) {
            starvenomore$setPlump(true);

            if (self.level() instanceof ServerLevel serverWorld) {

                serverWorld.playSound(null, self.getX(), self.getY(), self.getZ(),
                        SoundEvents.FUNGUS_BREAK, self.getSoundSource(),
                        5.0f, 0.5f);

                serverWorld.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        self.getX(), self.getY() + 0.2, self.getZ(),
                        16, 0.7, 0.1, 0.7, 0);



                serverWorld.sendParticles(
                        SpellParticleOption.create(ParticleTypes.EFFECT, 0xFFFFFFFF, 1.0F),
                        self.getX(), self.getY() + 0.5, self.getZ(),
                        24, 0.4, 1.0, 0.4, 0.05);
            }

        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void starvenomore$writePlump(ValueOutput view, CallbackInfo ci) {
        view.putInt("StarveNoMoreTicksSinceBreed", starvenomore$ticksSinceBreed);
        view.putBoolean("StarveNoMorePlump", starvenomore$isPlump());
        view.putBoolean("StarveNoMorePlayerLineage", starvenomore$isPlayerLineage());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void starvenomore$readPlump(ValueInput view, CallbackInfo ci) {
        starvenomore$ticksSinceBreed = view.getIntOr("StarveNoMoreTicksSinceBreed", 0);
        starvenomore$setPlump(view.getBooleanOr("StarveNoMorePlump", false));
        starvenomore$setPlayerLineage(view.getBooleanOr("StarveNoMorePlayerLineage", false));
    }
}
