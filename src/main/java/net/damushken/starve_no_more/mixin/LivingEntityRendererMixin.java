package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.command.ModConfig;
import net.damushken.starve_no_more.util.PlumpAccess;
import net.damushken.starve_no_more.util.PlumpUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.AgeableMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void starvenomore$scalePlump(LivingEntity livingEntity, LivingEntityRenderState state, float tickDelta, CallbackInfo ci) {
        if (livingEntity instanceof PlumpAccess plump && plump.starvenomore$isEffectivelyPlumpSynced()) {
            float plumpScale = plump.starvenomore$getSyncedScale();
            state.scale *= plumpScale;
        }
    }
}
