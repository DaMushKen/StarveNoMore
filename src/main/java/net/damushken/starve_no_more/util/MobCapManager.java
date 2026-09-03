package net.damushken.starve_no_more.util;

import net.damushken.starve_no_more.command.ModConfig;
import net.damushken.starve_no_more.mixin.SpawnGroupAccessor;
import net.minecraft.entity.SpawnGroup;

public class MobCapManager {

    public static void applyAll() {
        ModConfig cfg = ModConfig.get();
        ((SpawnGroupAccessor)(Object) SpawnGroup.CREATURE).setCapacity(cfg.creatureMaxCapacity);
        ((SpawnGroupAccessor)(Object) SpawnGroup.AXOLOTLS).setCapacity(cfg.axolotlsMaxCapacity);
        ((SpawnGroupAccessor)(Object) SpawnGroup.WATER_CREATURE).setCapacity(cfg.waterCreatureMaxCapacity);
        ((SpawnGroupAccessor)(Object) SpawnGroup.WATER_AMBIENT).setCapacity(cfg.waterAmbientMaxCapacity);
    }

}
