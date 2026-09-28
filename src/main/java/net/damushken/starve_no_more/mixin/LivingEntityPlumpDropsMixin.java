package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.StarveNoMore;
import net.damushken.starve_no_more.command.ModConfig;
import net.damushken.starve_no_more.util.PlumpAccess;
import net.damushken.starve_no_more.util.PlumpUtil;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityPlumpDropsMixin {

    @Inject(method = "dropFromLootTable", at = @At("TAIL"))
    private void starvenomore$multiplyPlumpDrops(ServerLevel world, DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object) this;
        if (!(self instanceof AgeableMob passive)) return;
        if (!PlumpUtil.isEffectivelyPlump(passive)) return;

        ModConfig cfg = ModConfig.get();
        if (!(self.level() instanceof ServerLevel serverWorld)) return;

        AABB box = self.getBoundingBox().inflate(1.5);
        for (ItemEntity itemEntity : serverWorld.getEntitiesOfClass(ItemEntity.class, box, e -> e.tickCount <= 1)) {
            ItemStack stack = itemEntity.getItem();
            int newCount = Math.round(stack.getCount() * cfg.plumpDropsMultiplier);
            stack.setCount(Math.min(newCount, stack.getMaxStackSize()));
        }
    }
}