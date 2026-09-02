package net.damushken.starve_no_more.mixin;

import net.minecraft.entity.SpawnGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * MIXIN class called in onIniziatilize() to change a certain spawn group max. capacity
 */

@Mixin(SpawnGroup.class)
public interface SpawnGroupAccessor {
    @Mutable
    @Accessor("capacity")
    void setCapacity(int capacity);
}
