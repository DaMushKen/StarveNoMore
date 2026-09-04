package net.damushken.starve_no_more.util;

import net.damushken.starve_no_more.command.ModConfig;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.entity.passive.PassiveEntity;

public class PlumpUtil {

    /**
     * True only if the animal has earned plump and is currently eligible to display/behave as plump.
     * The raw flag (PlumpAccess#starvenomore$isPlump) persists independently of eligibility,
     * so toggling config back on re-activates plump instantly without needing a fresh 2-day wait.
     */
    public static boolean isEffectivelyPlump(PassiveEntity entity) {
        if (!(entity instanceof PlumpAccess plump) || !plump.starvenomore$isPlump()) {
            return false;
        }

        ModConfig cfg = ModConfig.get();
        if (!cfg.doPlump) return false;
        if (!entity.getType().isIn(ModTags.EntityTypes.CAN_PLUMP)) return false;
        if (entity instanceof GoatEntity && !cfg.doGoatsDropAndPlump) return false;

        return true;
    }
}
