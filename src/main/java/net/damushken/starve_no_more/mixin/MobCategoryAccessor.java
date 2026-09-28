package net.damushken.starve_no_more.mixin;

import net.minecraft.world.entity.MobCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * MIXIN class called in onIniziatilize() to change a certain spawn group max. capacity
 */

@Mixin(MobCategory.class)
public interface MobCategoryAccessor {
    @Mutable
    @Accessor("max")
    void setMax(int capacity);
}
