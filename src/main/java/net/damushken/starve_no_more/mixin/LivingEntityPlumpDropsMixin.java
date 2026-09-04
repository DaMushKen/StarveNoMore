package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.StarveNoMore;
import net.damushken.starve_no_more.command.ModConfig;
import net.damushken.starve_no_more.util.PlumpAccess;
import net.damushken.starve_no_more.util.PlumpUtil;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityPlumpDropsMixin {

    @Inject(method = "dropLoot", at = @At("TAIL"))
    private void starvenomore$multiplyPlumpDrops(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object) this;
        if (!(self instanceof PassiveEntity passive)) return;
        if (!PlumpUtil.isEffectivelyPlump(passive)) return;

        ModConfig cfg = ModConfig.get();
        if (!(self.getWorld() instanceof ServerWorld serverWorld)) return;

        Box box = self.getBoundingBox().expand(1.5);
        for (ItemEntity itemEntity : serverWorld.getEntitiesByClass(ItemEntity.class, box, e -> e.age <= 1)) {
            ItemStack stack = itemEntity.getStack();
            int newCount = Math.round(stack.getCount() * cfg.plumpDropsMultiplier);
            stack.setCount(Math.min(newCount, stack.getMaxCount()));
        }
    }
}