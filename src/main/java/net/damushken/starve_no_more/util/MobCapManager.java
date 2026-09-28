package net.damushken.starve_no_more.util;

import net.damushken.starve_no_more.command.ModConfig;
import net.damushken.starve_no_more.mixin.MobCategoryAccessor;
import net.minecraft.world.entity.MobCategory;

public class MobCapManager {

    public static void applyAll() {
        ModConfig cfg = ModConfig.get();
        ((MobCategoryAccessor)(Object) MobCategory.CREATURE).setMax(cfg.creatureMaxCapacity);
        ((MobCategoryAccessor)(Object) MobCategory.AXOLOTLS).setMax(cfg.axolotlsMaxCapacity);
        ((MobCategoryAccessor)(Object) MobCategory.WATER_CREATURE).setMax(cfg.waterCreatureMaxCapacity);
        ((MobCategoryAccessor)(Object) MobCategory.WATER_AMBIENT).setMax(cfg.waterAmbientMaxCapacity);
    }

}
