package net.damushken.starve_no_more.util;

import net.damushken.starve_no_more.command.ModConfig;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.AgeableMob;

public class PlumpUtil {

    /**
     * True only if the animal has earned plump and is currently eligible to display/behave as plump.
     * The raw flag (PlumpAccess#starvenomore$isPlump) persists independently of eligibility,
     * so toggling config back on re-activates plump instantly without needing a fresh 2-day wait.
     */
    public static boolean isEffectivelyPlump(AgeableMob entity) {
        if (!(entity instanceof PlumpAccess plump) || !plump.starvenomore$isPlump()) {
            return false;
        }

        ModConfig cfg = ModConfig.get();
        if (!cfg.doPlump) return false;
        if (!entity.is(ModTags.EntityTypes.CAN_PLUMP)) return false;
        if (entity instanceof Goat && !cfg.doGoatsDropAndPlump) return false;
        if (!cfg.doWildPlump && !plump.starvenomore$isPlayerLineage()) return false;

        return true;
    }
}
